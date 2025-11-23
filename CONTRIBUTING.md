# StoreFlow Contribution Guide

Thank you for helping build StoreFlow! To maintain code quality and consistency, we strictly follow these guidelines.

## 🌿 Branching Strategy

We use short, specific branches. Never commit directly to `main`.

**Format:** `type/short-description-in-english`

| Type        | Usage                                 | Example                             |
|:------------|:--------------------------------------|:------------------------------------|
| `feat/`     | New feature                           | `feat/stock-decimal-support`        |
| `fix/`      | Bug fix                               | `fix/calculate-price-overflow`      |
| `chore/`    | Configuration/Maintenance             | `chore/update-pom-dependencies`     |
| `refactor/` | Code improvement without logic change | `refactor/extract-validation-logic` |
| `docs/`     | Documentation                         | `docs/update-readme`                |

---

## 💾 Commit Messages (Conventional Commits)

We follow the [Conventional Commits](https://www.conventionalcommits.org/) specification.

**Format:** `type(optional-scope): imperative description in lowercase`

* **Allowed Types:** `feat`, `fix`, `chore`, `refactor`, `docs`, `style`, `test`.
* **Rules:**
    * Use imperative mood ("add" not "added").
    * **Must be in English.**
    * No trailing period.

**Examples:**

* ✅ `feat(stock): add unit type enum to product`
* ✅ `fix: resolve null pointer in customer validation`
* ✅ `chore: update checkstyle rules`

---

## 🎫 Issue Management

Before writing code, an Issue must exist to justify it.

### Labels

* **Status:** `status:blocked` (⛔ Do not start), `status:in-progress`.
* **Type:** `type:bug`, `type:chore`.

### Workflow

1. Assign the Issue to yourself.
2. Move the Issue to the **"In Progress"** column.
3. Create a branch from `main` referencing the issue (optional but recommended): `feat/12-add-product-entity`.

---

## 🔀 Pull Requests

1. **Title:** Must follow the Commit format (e.g., `feat: Implement Unit Type Logic`).
2. **Description:** Use the provided template (Context, Changes, How to test).
3. **Review Process:**
    * CI (Maven + Checkstyle) must pass (Green).
    * No conflicts with `main`.
4. **Merge Strategy:** Use **"Squash and Merge"** to keep `main` history clean.

---

## ☕ Code Style (Java)

This project has strict rules validated by **Checkstyle**.

* **Language:** All code, comments, and Javadoc must be in **English**.
* **Formatting:** 4 spaces indentation, `UTF-8`.
* **Key Rules:**
    * 🚫 No line comments `//` allowed.
    * 🚫 No `System.out.println` (Use Loggers).
    * 🚫 Methods > 50 lines or High Cyclomatic Complexity (>10) will fail the build.
    * ✅ Javadoc is mandatory for public complex methods (>15 lines).

Run `mvn clean install` locally before pushing to verify compliance.
