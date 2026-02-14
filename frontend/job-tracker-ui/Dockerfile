FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
# OJO: ajusta "job-tracker-ui" si tu carpeta dist tiene otro nombre
COPY --from=build /app/dist/job-tracker-ui /usr/share/nginx/html
EXPOSE 80
