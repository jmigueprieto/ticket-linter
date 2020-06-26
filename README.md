# STORY LINTER

"Story Linter" is a Jira Cloud application built with Atlassian Connect. Checkout [this
video](https://www.youtube.com/watch?v=qzxVBjV5g60) for a quick intro.

As a [linter](https://en.wikipedia.org/wiki/Lint_(software)
analyzes source code to identify and report issues, like potential bugs, stylistic errors and suspicious constructs,
this Jira add-on will analyze your user stories and show warnings or alerts if any story does not follow (an opinionated)
set of rules or format.

## Expected  Format

The description of your Jira User Stories should be written following the well known User Story template (see: [User Stories with Examples and Template](https://www.atlassian.com/agile/project-management/user-stories)) 
and include an Acceptance Criteria which should be a bullet list of items that must be fulfilled to consider the story as done.


```text
“As a [persona], I [want to], [so that].”

AC
- Acceptance Criteria 1
- Acceptance Criteria 2
```


E.g.


![Sample User Story](./docs/images/sample-story.png)

## Tech Stack 

#### Backend
- Kotlin.
- Sprint Boot 2.3.0.
- MySQL 8.0.20.
- [**Atlassian Connect for Spring Boot**](https://developer.atlassian.com/cloud/jira/platform/frameworks-and-tools/)
_Handles tasks like JWT authentication and signing, persistence of host details, installation and uninstallation callbacks, and serving the app descriptor._
- GraalVM for Server Side React Rendering. (See [this post](https://medium.com/graalvm/improve-react-js-server-side-rendering-by-150-with-graalvm-58a06ccb45df))

#### Frontend
- ES6, JSX.
- ReactJS.

## Dev Environment Setup

- Create a MySQL container
```bash
docker pull mysql:8.0.20
docker run --name mysql-covidio -p 3306:3306 -e MYSQL_ROOT_PASSWORD='pazz' -e MYSQL_ROOT_HOST='%' -v /Users/jmpr/ticket-linter/data/mysql:/var/lib/mysql -d mysql:8.0.20
```
- Create a new user
```mysql
CREATE USER 'linter-app'@'%' IDENTIFIED BY 'xxxxxx';
GRANT ALL PRIVILEGES ON *.* TO 'linter-app'@'%' WITH GRANT OPTION;
```

- Create a database schema `covidio`

If Liquibase lock remains stuck

```mysql
use covidio;
UPDATE DATABASECHANGELOGLOCK SET LOCKED=0, LOCKGRANTED=null, LOCKEDBY=null where ID=1;
```

https://stackoverflow.com/questions/15528795/liquibase-lock-reasons