# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

CUI Portal UI is a multi-module Maven project providing JSF (Jakarta Faces) based UI components for the CUIOSS Portal framework. It targets Java 21+ and Jakarta EE 10.

- **Group ID:** `de.cuioss.portal.ui`
- **Parent POM:** `de.cuioss:cui-java-parent` (enforces checkstyle, code quality)
- **SonarQube:** project key `cuioss_cui-portal-ui`

## Build Commands

```bash
# Full build (all modules)
./mvnw clean install

# Code cleanup with OpenRewrite
./mvnw clean verify -Ppre-commit

# Build without tests
./mvnw clean install -DskipTests

# Build a single module (from repo root)
./mvnw clean install -pl modules/portal-ui-api

# Run tests for a single module
./mvnw test -pl modules/portal-ui-runtime

# Run a single test class
./mvnw test -pl modules/portal-ui-runtime -Dtest=SomeTestClass

# Run a single test method
./mvnw test -pl modules/portal-ui-runtime -Dtest=SomeTestClass#testMethodName

```

## Git Workflow

All cuioss repositories have branch protection on `main`. Direct pushes to `main` are never allowed. Always use this workflow:

1. Create a feature branch: `git checkout -b <branch-name>`
2. Commit changes: `git add <files> && git commit -m "<message>"`
3. Push the branch: `git push -u origin <branch-name>`
4. Create a PR: `gh pr create --repo cuioss/cui-portal-ui --head <branch-name> --base main --title "<title>" --body "<body>"`
5. Wait for CI + Gemini review (waits until checks complete): `gh pr checks --watch`
6. **Handle Gemini review comments** — fetch with `gh api repos/cuioss/cui-portal-ui/pulls/<pr-number>/comments` and for each:
    - If clearly valid and fixable: fix it, commit, push, then reply explaining the fix and resolve the comment
    - If disagree or out of scope: reply explaining why, then resolve the comment
    - If uncertain (not 100% confident): **ask the user** before acting
    - Every comment MUST get a reply (reason for fix or reason for not fixing) and MUST be resolved
7. Do **NOT** enable auto-merge unless explicitly instructed. Wait for user approval.
8. Return to main: `git checkout main && git pull`

## Module Structure

Root POM (`pom.xml`, packaging: pom) aggregates two sub-aggregators:

```
cui-portal-ui/
├── bom/                    # Bill of Materials - version management for all modules
└── modules/                # Parent for all implementation modules
    ├── portal-ui-api              # Core interfaces & API (ViewDescriptor, configuration, resources, dashboard widgets)
    ├── portal-ui-runtime          # Runtime implementations (page beans, config, listeners, view cache)
    ├── portal-ui-oauth            # OAuth2 authentication UI support
    ├── portal-ui-errorpages       # Default error page templates
    ├── portal-ui-form-based-login # Form-based login/logout UI
    ├── portal-ui-bootstrap-page-templates # Bootstrap Facelet page templates
    ├── portal-ui-unit-testing     # JUnit 5 extensions for portal UI testing
    └── portal-ui-quarkus-extension # Quarkus integration
```

**Dependency flow:** `portal-ui-api` is the foundation. `portal-ui-runtime` implements the API. Other modules depend on both. `portal-ui-unit-testing` provides test infrastructure used by sibling modules.

## Architecture

### CDI-Centric Design
Everything is wired through Jakarta CDI. Components use `@ApplicationScoped`, `@RequestScoped`, custom qualifiers (e.g., `@CuiCurrentView`, `@ConfigAsViewMatcher`), and producer methods. Tests use Weld CDI container via `weld-junit5`.

### Configuration
Uses MicroProfile Config. Portal-specific keys are prefixed with `portal.` and defined as constants in the API module. Default values live in `microprofile-config.properties` within `portal-ui-runtime`.

### View System
- `ViewDescriptor` / `PortalViewDescriptor` abstract JSF views
- `MultiTemplatingMapper` and `MultiViewMapper` enable flexible template resolution
- View matchers classify views (non-secured, transient, suppressed)
- `ViewListener` interface for lifecycle hooks (pre-render, post-load)

### Resource Handling
Custom resource handlers (`CacheableResource`, `NonCachableResource`, `CuiResource`) manage JSF resource serving with cache-busting and versioning.

### Key Packages (portal-ui-api)
- `de.cuioss.portal.ui.api.configuration` - Configuration types and qualifiers
- `de.cuioss.portal.ui.api.context` - JSF context providers (CurrentView, NavigationHandler)
- `de.cuioss.portal.ui.api.listener.view` - View lifecycle listeners
- `de.cuioss.portal.ui.api.resources` - Resource handler infrastructure
- `de.cuioss.portal.ui.api.dashboard` - Lazy-loading dashboard widget base classes
- `de.cuioss.portal.ui.api.templating` - Template mapping system

## Testing

- **Framework:** JUnit 5 with Weld CDI (`weld-junit5`)
- **Mocking:** EasyMock
- **Async testing:** Awaitility
- **CUI test libs:** `cui-test-value-objects` (bean/VO contracts), `cui-jsf-test-basic` (JSF mocks), `cui-test-juli-logger` (log assertions), `portal-core-unit-testing` (portal CDI test support)
- Tests are in standard `src/test/java` layout per module

## Key Dependencies

- **CUI JSF Components** (`de.cuioss.jsf:bom`): JSF component library version managed via `${version.cui.jsf.components}`
- **CUI Portal Core** (`de.cuioss.portal:bom`): Core portal framework version managed via `${version.cui.portal.core}`
- **Lombok:** Used throughout for `@Getter`, `@ToString`, `@EqualsAndHashCode`, `@UtilityClass`

## CI/CD

- GitHub Actions workflows in `.github/workflows/`
- Builds on Java 21 and 25
- SonarQube analysis on PRs
- Snapshots deploy to OSS Sonatype
- Releases publish to Maven Central via `release.yml`
- Configuration centralized in `.github/project.yml`
