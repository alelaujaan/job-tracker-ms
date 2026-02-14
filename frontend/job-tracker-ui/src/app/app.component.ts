import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Application } from './api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="max-width: 900px; margin: 20px auto; font-family: Arial;">
      <h2>Job Tracker Web</h2>

      <div style="display:flex; gap:10px; margin-bottom: 12px;">
        <input
          style="flex:1; padding: 10px;"
          [(ngModel)]="url"
          placeholder="Pega URL (InfoJobs / LinkedIn)..."
        />
        <button style="padding: 10px 14px;" (click)="importUrl()">Importar</button>
        <button style="padding: 10px 14px;" (click)="load()">Refrescar</button>
      </div>

      <div *ngIf="message" style="margin: 10px 0; padding: 10px; background:#f4f4f4;">
        {{ message }}
      </div>

      <h3>Candidaturas</h3>
      <table border="1" cellpadding="8" cellspacing="0" width="100%">
        <thead>
          <tr>
            <th>ID</th>
            <th>Empresa</th>
            <th>Título</th>
            <th>Fuente</th>
            <th>Estado</th>
            <th>URL</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let a of applications">
            <td>{{ a.id }}</td>
            <td>{{ a.company || '-' }}</td>
            <td>{{ a.title || '-' }}</td>
            <td>{{ a.source || '-' }}</td>
            <td>{{ a.status || '-' }}</td>
            <td><a [href]="a.url" target="_blank">abrir</a></td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
})
export class AppComponent {
  url = '';
  applications: Application[] = [];
  message = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.message = '';
    this.api.listApplications().subscribe({
      next: (data) => (this.applications = data),
      error: (err) => (this.message = `❌ Error listando: ${err?.message || err}`),
    });
  }

  importUrl() {
    const value = this.url.trim();
    if (!value) {
      this.message = '❌ Pega una URL primero.';
      return;
    }

    this.message = 'Importando...';
    this.api.importByUrl(value).subscribe({
      next: (created) => {
        this.message = `✅ Importado con id=${created.id}`;
        this.url = '';
        this.load();
      },
      error: (err) => (this.message = `❌ Error importando: ${err?.message || err}`),
    });
  }
}
