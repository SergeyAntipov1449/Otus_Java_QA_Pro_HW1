FROM maven:3.9.14-eclipse-temurin-21

USER root

RUN apt-get update && apt-get install -y --no-install-recommends \
    wget gnupg ca-certificates fonts-liberation python3 \
    libasound2t64 libatk-bridge2.0-0t64 libatk1.0-0t64 libcups2t64 \
    libdbus-1-3 libdbus-glib-1-2 libdrm2 libgbm1 libgtk-3-0t64 libnspr4 \
    libnss3 libx11-xcb1 libxcomposite1 libxcursor1 libxdamage1 libxfixes3 \
    libxi6 libxrandr2 libxshmfence1 libxss1 libxt6 libxtst6 xdg-utils \
    libpci3 libxcb1 libxcb-shm0 libxcb-shape0 \
    libxcb-xfixes0 libxcb-render0 libxcb-randr0 \
    libxcb-image0 libxcb-keysyms1 libxcb-xtest0 \
    libxcb-sync1 libxcb-util1 \
    && rm -rf /var/lib/apt/lists/*

RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && apt-get install -y google-chrome-stable --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

RUN FIREFOX_VERSION=$(wget -qO- https://product-details.mozilla.org/1.0/firefox_versions.json | python3 -c "import sys,json; print(json.load(sys.stdin)['LATEST_FIREFOX_VERSION'])") && \
    wget -q "https://download.mozilla.org/?product=firefox-${FIREFOX_VERSION}-ssl&os=linux64&lang=en-US" -O /tmp/firefox.tar.bz2 && \
    tar -xjf /tmp/firefox.tar.bz2 -C /opt/ && \
    ln -s /opt/firefox/firefox /usr/local/bin/firefox && \
    rm /tmp/firefox.tar.bz2

RUN GECKODRIVER_VERSION=$(wget -qO- https://api.github.com/repos/mozilla/geckodriver/releases/latest | python3 -c "import sys,json; print(json.load(sys.stdin)['tag_name'].lstrip('v'))") && \
    wget -q "https://github.com/mozilla/geckodriver/releases/download/v${GECKODRIVER_VERSION}/geckodriver-v${GECKODRIVER_VERSION}-linux64.tar.gz" -O /tmp/geckodriver.tar.gz && \
    tar -xzf /tmp/geckodriver.tar.gz -C /usr/local/bin/ && \
    chmod +x /usr/local/bin/geckodriver && \
    rm /tmp/geckodriver.tar.gz

RUN wget -q https://github.com/allure-framework/allure2/releases/download/2.32.0/allure-2.32.0.tgz && \
    tar -xzf allure-2.32.0.tgz -C /opt/ && \
    ln -s /opt/allure-2.32.0/bin/allure /usr/local/bin/allure && \
    rm allure-2.32.0.tgz

RUN mkdir -p /app
WORKDIR /app

COPY . /app
RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]