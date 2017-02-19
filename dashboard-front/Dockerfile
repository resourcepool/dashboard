FROM node:7.4.0

EXPOSE 9000

RUN npm install -g gulp

RUN npm install -g bower

COPY . /data

WORKDIR /data

RUN npm install

RUN bower --allow-root install

CMD ["gulp", "serve"]