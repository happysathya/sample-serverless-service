# AWS serverless handler template

##### Build
```
./gradlew clean build
```

##### Deploy
```
serverless info --stage dev
serverless remove --stage dev
serverless deploy --stage dev
```

##### Travis CI Status

[![Build Status](https://travis-ci.org/happysathya/serverless-java-template.svg?branch=master)](https://travis-ci.org/happysathya/serverless-java-template)
