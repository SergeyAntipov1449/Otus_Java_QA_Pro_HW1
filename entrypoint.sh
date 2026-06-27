#!/bin/bash

BASE_URL=""
BROWSER="chrome"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --base_url)
      BASE_URL="$2"
      shift 2
      ;;
    --browser)
      BROWSER="$2"
      shift 2
      ;;
    *)
      shift
      ;;
  esac
done

mvn clean test \
  -Dbase.url="${BASE_URL:-https://otus.ru}" \
  -Dbrowser="${BROWSER:-chrome}" \
  -Dheadless=true