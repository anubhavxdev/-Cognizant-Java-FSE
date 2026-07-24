# Lab 1: Git Installation, Configuration & Basic Commands

> **Module:** 1. GIT-HOL  
> **Estimated Time:** 30 Minutes  
> **Prerequisites:** Git Bash client installed on Windows machine  
> **Objectives:** Familiarize with Git commands: `git init`, `git status`, `git add`, `git commit`, `git push`, `git pull`, user setup, and editor integration (`notepad++.exe`).

---

## 🛠️ Step 1: Setup Machine with Git Configuration

### 1. Verify Git Installation
Open **Git Bash** and check the installed Git version:
```bash
git --version
```
**Expected Output:**
```text
git version 2.42.0.windows.1
```

### 2. Configure Global User Identity (Name & Email)
Set your global username and email address (do not use corporate/Cognizant credentials for personal GitHub/GitLab accounts):
```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### 3. Verify Identity Configuration
Verify that identity settings are properly saved in the global configuration:
```bash
git config --global --list
```
**Expected Output:**
```text
user.name=Your Name
user.email=your.email@example.com
```

---

## 📝 Step 2: Integrate `notepad++.exe` as Default Git Editor

### 1. Test Notepad++ Execution from Git Bash
Run Notepad++ directly from Git Bash:
```bash
notepad++.exe
```
> **Note:** If Git Bash displays `command not found`, add Notepad++'s installation directory (`C:\Program Files\Notepad++`) to the Windows **Environment Variables** (System Properties → Advanced → Environment Variables → Path → Edit).

### 2. Set Notepad++ as Default Git Global Editor
Configure Git to open Notepad++ whenever a multi-line commit message or interactive rebase requires an editor:
```bash
git config --global core.editor "'C:/Program Files/Notepad++/notepad++.exe' -multiInst -notabbar -nosession -noPlugin"
```

### 3. Verify Editor Configuration
Inspect the global Git configuration file using the configured editor:
```bash
git config --global --list
```
Or open the configuration file directly in the editor:
```bash
git config --global -e
```
**Output (`.gitconfig` contents):**
```ini
[user]
    name = Your Name
    email = your.email@example.com
[core]
    editor = 'C:/Program Files/Notepad++/notepad++.exe' -multiInst -notabbar -nosession -noPlugin
```

---

## 📂 Step 3: Create, Track & Commit Source Code Repository (`GitDemo`)

### 1. Create and Initialize "GitDemo" Project Directory
Create a new directory and initialize a Git repository inside it:
```bash
mkdir GitDemo
cd GitDemo
git init
```
**Expected Output:**
```text
Initialized empty Git repository in C:/.../GitDemo/.git/
```

### 2. Verify Repository Initialization
Check hidden files in the working directory to confirm the `.git` folder exists:
```bash
ls -la
```
**Expected Output:**
```text
drwxr-xr-x 1 user 197121  0 Jul 24 14:30 .
drwxr-xr-x 1 user 197121  0 Jul 24 14:30 ..
drwxr-xr-x 1 user 197121  0 Jul 24 14:30 .git
```

### 3. Create `welcome.txt` File
Create a new text file and write sample content:
```bash
echo "Welcome to Git Hands-On Learning!" > welcome.txt
echo "This is a sample file created for Git HOL 1." >> welcome.txt
```

### 4. Verify File Creation & Content
Check that the file exists and verify its contents:
```bash
ls -l
cat welcome.txt
```
**Expected Output:**
```text
Welcome to Git Hands-On Learning!
This is a sample file created for Git HOL 1.
```

### 5. Check Working Directory Status
Check repository status before staging:
```bash
git status
```
**Expected Output:**
```text
On branch main (or master)

No commits yet

Untracked files:
  (use "git add <file>..." to include in what will be committed)
	welcome.txt

nothing added to commit but untracked files present (use "git add" to track)
```

### 6. Stage `welcome.txt` for Tracking
Add `welcome.txt` to the Git staging area (Index):
```bash
git add welcome.txt
git status
```
**Expected Output:**
```text
On branch main

No commits yet

Changes to be committed:
  (use "git rm --cached <file>..." to unstage)
	new file:   welcome.txt
```

### 7. Commit Staged File
Commit the file with a descriptive commit message:
```bash
git commit -m "Initial commit: Add welcome.txt with introductory message"
```
**Expected Output:**
```text
[main (root-commit) a1b2c3d] Initial commit: Add welcome.txt with introductory message
 1 file changed, 2 insertions(+)
 create mode 100644 welcome.txt
```

### 8. Verify Commit History & Repository Clean Status
Confirm that the working tree is clean and review the commit history:
```bash
git status
git log --oneline
```
**Expected Output:**
```text
On branch main
nothing to commit, working tree clean

a1b2c3d Initial commit: Add welcome.txt with introductory message
```

---

## 🌐 Remote Repository Operations (GitLab / GitHub)

### 1. Add Remote Repository
Link the local repository to your remote platform (GitLab/GitHub):
```bash
git remote add origin https://gitlab.com/your-username/GitDemo.git
```

### 2. Pull Remote Branch (Synchronize)
Pull any initial files (e.g., remote `README.md` or license):
```bash
git pull origin main --allow-unrelated-histories
```

### 3. Push Local Commits to Remote Repository
Push local commits to the remote master/main branch:
```bash
git push -u origin main
```

---

## 📁 Lab Artifacts Included in this Folder

- [`GitDemo/welcome.txt`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/1.%20Git-HOL/GitDemo/welcome.txt) — Sample text file created and tracked during the lab.
- [`git_setup_demo.sh`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/1.%20Git-HOL/git_setup_demo.sh) — Shell script automating the setup and demo commands.
