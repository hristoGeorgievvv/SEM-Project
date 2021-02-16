echo "---- Starting Integration Tests ----"
echo "---- Building images ----"
docker-compose up --build -d
echo "---- Waiting 20s for initialization ----"
sleep 20
gradle :integration:test
