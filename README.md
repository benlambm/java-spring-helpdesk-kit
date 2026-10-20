# Spring Boot Help Desk Kit

**Coding Homework 5, Part B (extra credit).** Coding Homework 3 kept the help
desk's tickets in a CSV file and Java collections. Here you move them into a
database table with **Spring Boot** and Spring's **`JdbcClient`**, and GitHub
Actions proves it works on every push.

The project opens in **Eclipse**, **VS Code**, or **Cursor**. It uses Java 21
and Maven through the included wrapper, so there's nothing extra to install.
Tests run against an in-memory H2 database, so you don't need PostgreSQL on
your computer.

## What's already done, and what's yours

| Already done | Your job |
| --- | --- |
| `pom.xml` (Spring Boot 4.1.1), the Maven wrapper, `schema.sql` | Paste in your four finished Coding Homework 3 classes |
| `HelpDeskApplication`, `HelpDeskRunner`, `TicketController` | Write six `TicketRepository` methods with `JdbcClient` |
| `TicketRepository.count()`, a worked example | Write `TicketImporter.importCsv` |
| 35 tests: your original 19, plus 16 new ones | Add a GitHub Actions workflow and make all 35 pass there |

## Step 1: Make your own copy

1. On this repository's GitHub page, choose **Use this template**, then
   **Create a new repository**. Public is fine. If you make it private, add
   **benlambm** as a collaborator (**Settings > Collaborators**) so your
   instructor can see it.
2. Clone your new repository.
   - **Eclipse:** File > Import > Git > Projects from Git (with smart import).
     Eclipse spots `pom.xml` and imports a Maven project.
   - **VS Code / Cursor:** choose Clone Repository from the Welcome screen.

The first build downloads Spring Boot, so give it a few minutes.

## Running the tests

- **Eclipse:** right-click `src/test/java` > Run As > JUnit Test
- **VS Code / Cursor:** the Testing beaker
- **Terminal:** `./mvnw test` on macOS, Linux, or Git Bash, or `.\mvnw.cmd test`
  in Windows PowerShell

On a fresh copy, exactly one test passes: `countReturnsTheNumberOfRows`, which
checks the worked example. The other 34 are your to-do list.

## Step 2: Bring your Coding Homework 3 code

Copy your four finished Coding Homework 3 classes into
`src/main/java/edu/vwcc/itp220mod4/`, replacing the stubs:

`TicketCsvReader.java`, `TicketQueries.java`, `TicketStats.java`,
`TicketReportWriter.java`

Same package, nothing to change. Run the tests again: your original 19 turn
green, for 20 of 35.

## Step 3: Write the database code

Work one test class at a time.

1. **`TicketRepository`**: six methods. Read the worked example, `count()`,
   first. `TicketRepositoryTest` is the specification.
2. **`TicketImporter.importCsv`**: read the CSV with your `TicketCsvReader`,
   clear the table, and save every ticket. `TicketImporterTest` checks it,
   including what `@Transactional` does when an import fails halfway.

Every `JdbcClient` call has the same shape: SQL, then parameters, then the
kind of result you want.

| You want | JdbcClient |
| --- | --- |
| Insert, update, or delete | `jdbc.sql("...").params(a, b, c).update()` |
| One row, or none | `.query(TicketRepository::mapRow).optional()` |
| A list of rows | `.query(TicketRepository::mapRow).list()` |
| One column of values | `.query(String.class).list()` |
| One value | `.query(Integer.class).single()` |

Put every value in as a `?` parameter. Never glue values into the SQL string.
One of the tests checks for that.

When `HelpDeskApplicationTest` turns green, all 35 pass. Take a victory lap:

- **Run it:** `./mvnw spring-boot:run`, or run `HelpDeskApplication` from your
  IDE. It prints the same summary as Coding Homework 3 and writes
  `output/category-summary.txt`.
- **Open** <http://localhost:8080/> and then
  <http://localhost:8080/api/tickets/open>. Stop the app with Ctrl+C.

## Step 4: Run the tests on GitHub

Your Part A workflow compiled with `javac`. This project builds with Maven, so
the test step becomes one command. Create `.github/workflows/ci.yml` that, on
every push:

1. checks out the code;
2. installs Java 21 with `actions/setup-java` (add `cache: maven` to speed up
   later runs);
3. runs `mvn -B test`.

Push, open the **Actions** tab, and wait for the green check. Inside the run,
the test step's last lines should read `Tests run: 35, Failures: 0, Errors: 0`.
That run's link is what you submit in Canvas.

## Bonus: deploy only when the tests pass

The goal: every push to `main` runs the tests, and **only if they pass** does
the new version go live on the internet, backed by a real PostgreSQL database.
The steps below use Render's free tier. Free plans change, so check Render's
current terms when you sign up.

1. **Create the database.** In Render, choose **New > Postgres**, instance type
   **Free**. Free databases expire 30 days after you create them, which is
   plenty for this assignment.
2. **Create the web service.** Choose **New > Web Service**, connect your
   GitHub repository, pick **Docker** as the language (this kit's `Dockerfile`
   builds and runs the app), and choose instance type **Free**.
3. **Connect the app to the database.** Add three environment variables to the
   web service, using the values on the database's connection page:
   - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://` + the internal hostname +
     `:5432/` + the database name
   - `SPRING_DATASOURCE_USERNAME`: the database username
   - `SPRING_DATASOURCE_PASSWORD`: the database password

   Render also shows a ready-made URL that starts with `postgresql://`. Java
   can't use that one: it needs the `jdbc:` prefix, with the username and
   password kept separate.
4. **Stop automatic deploys.** In the web service's Settings, set
   **Auto-Deploy** to **Off**. From now on, your workflow decides.
5. **Save the deploy hook as a secret.** In the web service's Settings, copy
   the **Deploy Hook** URL and treat it like a password. In GitHub, open
   **Settings > Secrets and variables > Actions > New repository secret**. Name
   it `RENDER_DEPLOY_HOOK_URL` and paste the URL as its value.
6. **Add a deploy job** to your workflow. It must:
   - wait for the test job (`needs: test`);
   - run only for pushes to `main`
     (`if: github.event_name == 'push' && github.ref == 'refs/heads/main'`);
   - call the hook with `curl --fail --silent --show-error`, reading the secret
     through an `env:` variable rather than typing it into the command.
7. **Push.** The Actions run now shows **test**, then **deploy**. Render takes
   a few minutes to build the image, then your app is live at
   `https://YOUR-SERVICE-NAME.onrender.com/`.

Free services go to sleep after 15 minutes without visitors. The first visit
after that takes a minute or two while the app wakes up.

Prefer Railway? That works too. Its builder reads `pom.xml`, or uses this
`Dockerfile`. Just make sure deploys still wait for your tests.

## Troubleshooting

- **`./mvnw: Permission denied`:** run `chmod +x mvnw`. If GitHub Actions says
  it, run `git update-index --chmod=+x mvnw`, then commit and push.
- **Eclipse shows red errors right after import:** right-click the project >
  Maven > Update Project.
- **Tests pass on your computer but fail on GitHub:** run `git status`. You
  probably haven't committed a file yet.
- **The deploy succeeds but the app crashes on startup:** recheck the three
  `SPRING_DATASOURCE_` variables. The URL must start with `jdbc:postgresql://`.

What to submit is in the Canvas assignment.
