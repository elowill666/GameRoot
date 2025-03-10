# 使用 Node.js 作為基礎映像來構建應用
FROM node:16 AS build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

# 構建應用
RUN npm run build

# 使用 Nginx 作為基礎映像來運行應用
FROM nginx:alpine AS production-stage

COPY --from=build-stage /app/dist /usr/share/nginx/html

# nginx 配置文件
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf

# 啟動 Nginx
CMD ["nginx", "-g", "daemon off;"]