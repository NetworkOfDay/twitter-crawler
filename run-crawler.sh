#!/bin/bash

cd /home/fahrer/bin

pidfile=/home/fahrer/bin/pidfile


kill -TERM $(cat $pidfile)

echo $$ > $pidfile

exec java -jar twitter-crawler.jar /srv/data/twitter/wdt/ >> info.log 2>> error.log

