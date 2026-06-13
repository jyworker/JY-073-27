# Gitember v3

> A free, open-source Git GUI client for Linux, Windows, and macOS. Fast, lightweight, and packed with features that make everyday Git work a pleasure.

![Gitember](site/assets/img/3/repo-view.png)

## Overview

**Gitember** is a cross-platform Git GUI client built with **Java Swing** and **FlatLaf**. It provides a clean visual interface for common Git operations — commit, branch, merge, rebase, stash, and more — without requiring command-line memorisation.

> **What's new in v3:** The GUI has been completely rewritten, replacing the heavy JavaFX runtime. The result is a significantly smaller download, faster startup, and lower memory usage — while adding first-class integrations with **GitHub, GitLab, Bitbucket, and Gitea**. JavaFX version still available in the branch or by tag.

## Features

### Core Git Operations
- **Commit, Branch, Merge, Stash, Rebase** — all through a clean visual interface
- **Interactive Rebase** — reorder, squash, reword, edit, drop commits visually
- **3-Way Conflict Resolution** — built-in merge conflict editor
- **Worktree Support** — manage multiple working trees
- **Git LFS** — manage large binary assets with built-in LFS tooling

### Diff & Comparison
- **Powerful Diff Viewer** — unified, context, and side-by-side modes with syntax highlighting for 40+ languages
- **Folder Comparison** — compare entire directory trees, spot added/removed/changed files at a glance
- **Arbitrary File Comparison** — compare any two files outside of Git

### Remote Integration
- **Pull Request Review** — browse and review PRs from GitHub, GitLab, Bitbucket, and self-hosted Gitea without leaving the app
- **Remote Management** — manage remotes, fetch, pull, push with ease

### Search & Analytics
- **Advanced Full-Text Search** — search commits, file contents, Office documents, PDFs, images, and CAD files across the entire history (powered by Apache Lucene & Tika)
- **Repository Statistics** — commit frequency, lines changed per author, branch activity

### AI (Experimental)
- **Secret Leak Detection** — scan commits for accidentally committed secrets and credentials
- **Branch Difference Description** — AI-generated summary of differences between branches

## System Requirements

- **Java Runtime**: JDK 21 or later
- **OS**: Linux (x64), Windows (x64), macOS (ARM64)
- **Memory**: 256 MB minimum, 512 MB+ recommended

## Build from Source

### Prerequisites

- JDK 21+ ([Download](https://adoptium.net/))
- Apache Maven 3.8+

### Build & Run

```bash
# Clone the repository
git clone https://github.com/iazarny/gitember.git
cd gitember

# Compile
mvn compile

# Package as executable JAR
mvn package -DskipTests

# Run
java -jar target/gitember-3.2-SNAPSHOT.jar
```

The fat JAR is also available for download (see below).

## Downloads — Version 3.2

| Platform         | Link |
|------------------|------|
| Windows (x64)    | [Gitember-3.2.msi](https://gitember.org/Gitember-3.2.msi) or [Microsoft Store](https://apps.microsoft.com/detail/9NXNMLLGBGD4) |
| macOS (ARM64)    | [Gitember-3.2.dmg](https://gitember.org/Gitember-3.2.dmg) |
| Linux (x64)      | [Gitember-3.2.deb](https://gitember.org/Gitember-3.2.deb) |
| Fat JAR (Java 21) | [Gitember-3.2.jar](https://gitember.org/Gitember-3.2.jar) |

More info and documentation: [https://gitember.org](https://gitember.org)

## Changelog

### 3.2 · May 2026
- Added 3-way merge for conflict resolving
- Worktree support
- AI (experimental)
  - Secret leak detection
  - Branch difference description

### 3.1 · April 2026
- Interactive rebase
- Secret leak detection (experimental)
- Author and committer overwrite
- Speedup statistics up to 3×
- Small bug fixes

### 3.0 · March 2026
- **GUI migrated from JavaFX to Swing** — dramatically faster startup, smaller install, lower memory footprint
- **GitHub, GitLab, Bitbucket, Gitea integration** — browse and review Pull Requests directly inside Gitember, avatars, token support
- Improved rendering performance across all platforms
- Reduced installer size

### 2.x.x
The JavaFX version is available by tags in the repository.

## Project Structure

```
├── pom.xml                  # Maven build configuration
├── src/                     # Java source code
│   └── main/java/com/az/gitember/
│       ├── App.java         # Application entry point
│       ├── component/       # Reusable UI components
│       ├── data/            # Data models and repositories
│       ├── dialog/          # Dialog windows
│       ├── handler/         # Event handlers and controllers
│       ├── service/         # Business logic services
│       └── ui/              # Main UI windows
├── inst/                    # Installer scripts
├── docs/                    # Documentation and marketing materials
├── site/                    # Website assets
└── ge-doc/                  # Documentation site
```

## License

This project is licensed under **Apache License 2.0** and **GNU General Public License v3.0** — see [LICENSE-Apache-2.0.txt](LICENSE-Apache-2.0.txt) and [LICENSE-Gnu-3.0.txt](LICENSE-Gnu-3.0.txt) for details.
"}