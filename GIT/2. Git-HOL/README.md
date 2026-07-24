# Lab 2: Managing Unwanted Files using `.gitignore`

> **Module:** 2. GIT-HOL  
> **Estimated Time:** 20 Minutes  
> **Prerequisites:** Git environment configured, Notepad++ integrated, local & remote Git repository setup  
> **Objectives:**  
> • Understand the concept and importance of `.gitignore`.  
> • Learn how to exclude unwanted files (logs, build outputs, temporary files) from version control.  
> • Implement and verify `.gitignore` pattern matching rules.

---

## 📖 Theory & Core Concepts

### What is `.gitignore`?
A `.gitignore` file is a plain text file placed in the root (or subdirectories) of a Git repository. It tells Git which untracked files and folders to intentionally ignore and exclude from version control.

### Why do we ignore files?
1. **Prevent Noise:** Keeps the repository clean by excluding build artifacts, compiled binaries (`.class`, `.jar`, `.exe`), and dependency folders (`node_modules/`, `target/`).
2. **Security:** Prevents sensitive data (API keys, database credentials, secret keys, `.env` files) from accidentally being committed to public or team repositories.
3. **Save Storage & Bandwidth:** Excludes large dynamic files like log outputs (`*.log`), OS cache (`.DS_Store`, `Thumbs.db`), and temp files.

### Common `.gitignore` Pattern Rules

| Pattern | Rule Explanation | Example Match |
|---|---|---|
| `*.log` | Ignores all files with `.log` extension in any directory | `app.log`, `error.log`, `logs/system.log` |
| `logs/` | Ignores the `logs` directory and all files/subfolders inside it | `logs/debug.log`, `logs/2026/app.log` |
| `/build` | Ignores `build` folder only at the root level | `/build/out.jar` |
| `temp?.txt` | Wildcard matching single character | `temp1.txt`, `tempA.txt` |
| `!important.log` | Negation — includes `important.log` even if `*.log` is ignored | `important.log` |
| `**/*.tmp` | Matches `.tmp` files across all nested directory levels | `a/b/c/temp.tmp` |

---

## 🧪 Hands-On Lab Walkthrough

### Step 1: Create Source Code & Unwanted Log Files

Open **Git Bash** and navigate to your `GitDemo` project directory:
```bash
cd GitDemo
```

Create a sample source file `app.py`:
```bash
echo 'print("Application started")' > app.py
```

Create an untracked log file (`app.log`) and a log directory (`logs/debug.log`):
```bash
echo "[INFO] Application log entry" > app.log
mkdir -p logs
echo "[DEBUG] Debug log entry" > logs/debug.log
```

---

### Step 2: Check `git status` Before `.gitignore`

Run `git status` to see all newly created files:
```bash
git status
```

**Output (Before `.gitignore`):**
```text
On branch main

Untracked files:
  (use "git add <file>..." to include in what will be committed)
	app.log
	app.py
	logs/

nothing added to commit but untracked files present (use "git add" to track)
```
> **Notice:** Both `app.log` and the `logs/` folder are listed as untracked files.

---

### Step 3: Create and Update `.gitignore` File

Create or edit the `.gitignore` file using Notepad++ or command line:
```bash
notepad++.exe .gitignore
```

Add the following rules to ignore `.log` extensions and log directories:
```gitignore
# Ignore all files with .log extension
*.log

# Ignore log directories and their contents
logs/
log/

# Ignore temporary files
*.tmp
```

---

### Step 4: Verify `git status` After `.gitignore`

Check `git status` to verify that `app.log` and `logs/` are no longer listed:
```bash
git status
```

**Output (After `.gitignore`):**
```text
On branch main

Untracked files:
  (use "git add <file>..." to include in what will be committed)
	.gitignore
	app.py

nothing added to commit but untracked files present (use "git add" to track)
```
> **Success:** `app.log` and `logs/` are now completely hidden from Git tracking!

---

### Step 5: Verify Rule Matching with `git check-ignore`

To test which specific rule in `.gitignore` is ignoring a file, execute:
```bash
git check-ignore -v app.log
git check-ignore -v logs/debug.log
```

**Output:**
```text
.gitignore:2:*.log      app.log
.gitignore:5:logs/      logs/debug.log
```

---

### Step 6: Stage, Commit & Push `.gitignore` and Tracked Files

Add `.gitignore` and `app.py` to staging area:
```bash
git add .gitignore app.py
git status
```

Commit the changes:
```bash
git commit -m "Add source code app.py and configure .gitignore for log files"
```

Push to remote repository:
```bash
git push origin main
```

---

## 📁 Lab Artifacts Included in this Folder

- [`GitDemo/.gitignore`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/2.%20Git-HOL/GitDemo/.gitignore) — The `.gitignore` file configured with `*.log` and `logs/` rules.
- [`GitDemo/app.py`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/2.%20Git-HOL/GitDemo/app.py) — Tracked source code file.
- [`GitDemo/app.log`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/2.%20Git-HOL/GitDemo/app.log) — Sample ignored log file.
- [`GitDemo/logs/debug.log`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/2.%20Git-HOL/GitDemo/logs/debug.log) — Sample ignored log file inside `logs/` directory.
- [`git_ignore_demo.sh`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/2.%20Git-HOL/git_ignore_demo.sh) — Shell script automating the demonstration of `.gitignore`.
