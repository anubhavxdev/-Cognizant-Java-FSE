# Lab 5: Remote Repository Synchronization & Cleanup

> **Module:** 5. GIT-HOL  
> **Estimated Time:** 10 Minutes  
> **Prerequisites:** Hands-on ID "Git-T03-HOL_002", Git Bash, local & remote repository setup  
> **Objectives:**  
> • Learn how to verify clean repository states before pushing.  
> • Synchronize local and remote master/main branches using `git pull` and `git push`.  
> • Practice branch hygiene and verify remote repository reflection.

---

## 📖 Theory & Core Concepts

### 1. The Need for Remote Synchronization & Cleanup

In professional software development, multiple team members push commits to shared remote repositories (GitHub/GitLab). To prevent diverged histories and broken builds:
- **Pull Before Push:** Always fetch and pull remote changes (`git pull`) into your local `main` branch before pushing your local commits.
- **Clean Working Tree:** Uncommitted or temporary files should be stashed, committed, or ignored before performing sync operations.
- **Branch Hygiene:** Temporary feature branches should be deleted locally (`git branch -d <branch>`) and remotely (`git push origin --delete <branch>`) once merged.

### 2. Mechanics of `git pull` and `git push`

```
  Local Repository                      Remote Repository (GitHub/GitLab)
┌──────────────────┐                       ┌──────────────────┐
│  Working Tree    │                       │                  │
│       │          │   git push origin     │   origin/main    │
│       ▼          │──────────────────────►│                  │
│   Local Commit   │                       │                  │
│       ▲          │   git pull origin     │                  │
│       │          │◄──────────────────────│                  │
└──────────────────┘                       └──────────────────┘
```

- **`git pull origin main`:** Fetches commits from the remote `main` branch and merges them into your local active branch.
- **`git push origin main`:** Uploads local commits from your local `main` branch to the remote repository.

---

## 🧪 Hands-On Lab Walkthrough (Steps 1–5)

### Step 1: Verify `master` (or `main`) is in a Clean State

Open **Git Bash** in the `GitDemo` project directory:
```bash
cd GitDemo
git status
```
**Expected Output:**
```text
On branch main (or master)
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```
> **Explanation:** Verifies that no untracked or uncommitted changes remain in the working directory.

---

### Step 2: List Out All Available Branches

List all local and remote branches to inspect current repository structure:
```bash
git branch -a
```
**Expected Output:**
```text
* main
  remotes/origin/main
```
> **Explanation:** Shows all local branches alongside remote-tracking branches (`remotes/origin/*`). The `*` symbol marks the currently checked-out branch.

---

### Step 3: Pull Remote Repository Changes to `master`

Synchronize local `master`/`main` with any remote updates pushed by team members:
```bash
git pull origin main
```
**Expected Output:**
```text
From https://github.com/anubhavxdev/-Cognizant-Java-FSE
 * branch            main       -> FETCH_HEAD
Already up to date.
```
> **Explanation:** Executes `git fetch` followed by `git merge` to integrate upstream changes smoothly.

---

### Step 4: Push Pending Local Changes to Remote Repository

Push local commits resulting from `Git-T03-HOL_002` and merge conflict resolutions to remote:
```bash
git push origin main
```
**Expected Output:**
```text
Enumerating objects: 15, done.
Counting objects: 100% (15/15), done.
Delta compression using up to 12 threads
Compressing objects: 100% (10/10), done.
Writing objects: 100% (15/15), 2.5 KiB | 2.50 MiB/s, done.
Total 15 (delta 4), reused 0 (delta 0), pack-reused 0
To https://github.com/anubhavxdev/-Cognizant-Java-FSE.git
   d2406c6..b6d81e7  main -> main
```

---

### Step 5: Observe and Verify Changes Reflected in Remote

Verify the updated commit tree graph and confirm the remote tracking branch `origin/main` points to `HEAD`:
```bash
git log --oneline --graph --decorate -n 5
```
**Expected Output:**
```text
* b6d81e7 (HEAD -> main, origin/main) Add GIT module containing Hands-On Learning (HOL) exercises 1 to 5
* d2406c6 Complete Git HOL 4 assignment: Merge conflict resolution in hello.xml
* e2de4e7 Complete Git HOL 3 assignment: Branching & Merging
* 3017d9d Complete Git HOL 2 assignment: .gitignore rules
* 0f48b07 Complete Git HOL 1 assignment: Git configuration and welcome.txt
```

Verify final clean working tree status:
```bash
git status
```
**Expected Output:**
```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

---

## 📁 Lab Artifacts Included in this Folder

- [`GitDemo/cleanup_summary.txt`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/5.%20Git-HOL/GitDemo/cleanup_summary.txt) — Verification summary checklist.
- [`git_remote_cleanup_push_demo.sh`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/5.%20Git-HOL/git_remote_cleanup_push_demo.sh) — Shell script automating the 5-step pull, push, and status verification process.
