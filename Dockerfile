FROM maven:3.9.14-eclipse-temurin-21

USER root

RUN apt-get update && apt-get install -y --no-install-recommends \
    wget gnupg ca-certificates fonts-liberation \
    libasound2t64 libatk-bridge2.0-0t64 libatk1.0-0t64 libcups2t64 \
    libdbus-1-3 libdrm2 libgbm1 libgtk-3-0t64 libnspr4 \
    libnss3 libxcomposite1 libxdamage1 libxfixes3 \
    libxrandr2 libxss1 libxtst6 xdg-utils \
    libpci3 libxcb1 libxcb-shm0 libxcb-shape0 \
    libxcb-xfixes0 libxcb-render0 libxcb-randr0 \
    libxcb-image0 libxcb-keysyms1 libxcb-xtest0 \
    libxcb-sync1 libxcb-util1 \
    && rm -rf /var/lib/apt/lists/*

RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && apt-get install -y google-chrome-stable --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

RUN apt-get update && apt-get install -y firefox --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

RUN wget -q https://github.com/mozilla/geckodriver/releases/download/v0.35.0/geckodriver-v0.35.0-linux64.tar.gz && \
    tar -xzf geckodriver-v0.35.0-linux64.tar.gz -C /usr/local/bin/ && \
    rm geckodriver-v0.35.0-linux64.tar.gz

RUN wget -q https://github.com/allure-framework/allure2/releases/download/2.32.0/allure-2.32.0.tgz && \
    tar -xzf allure-2.32.0.tgz -C /opt/ && \
    ln -s /opt/allure-2.32.0/bin/allure /usr/local/bin/allure && \
    rm allure-2.32.0.tgz

RUN mkdir -p /root/ui_test
WORKDIR /root/ui_test

COPY . /root/ui_test
RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]