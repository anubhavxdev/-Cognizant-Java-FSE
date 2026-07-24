# Lab 3: Git Branching, Merging, and Merge Requests (MR / PR)

> **Module:** 3. GIT-HOL  
> **Estimated Time:** 30 Minutes  
> **Prerequisites:** Git environment configured, P4Merge tool installed (or configured as diff tool)  
> **Objectives:**  
> • Understand Git Branching & Merging concepts.  
> • Learn GitLab Merge Requests (MR) and GitHub Pull Requests (PR) workflows.  
> • Create a feature branch (`GitNewBranch`), make changes, diff, merge into `main`/`master`, view commit graph, and safely delete the branch.

---

## 📖 Theory & Core Concepts

### 1. What is Branching and Merging?

- **Branching:** A branch represents an independent line of development. It allows developers to isolate new features, bug fixes, or experiments from the production-ready main branch (`main` or `master`). Changes on a branch do not affect other branches until explicitly merged.
- **Merging:** Merging integrates commit histories from a source branch (e.g., `GitNewBranch`) into a target branch (e.g., `main`).
  - **Fast-Forward Merge:** Occurs when target branch has no new commits since branching. Git simply moves the target pointer forward.
  - **3-Way Merge:** Occurs when both source and target branches have diverged. Git creates a dedicated **Merge Commit** combining changes from both.

### 2. GitLab Merge Requests (MR) & GitHub Pull Requests (PR)

In modern enterprise workflows, developers do not push directly to `main` or merge branches locally without review:
1. **Branch Creation:** Developer creates a topic branch (e.g., `feature/user-auth`) locally and pushes it to remote (`git push -u origin feature/user-auth`).
2. **Create Merge Request (MR / PR):**
   - On **GitLab:** Navigate to **Merge Requests** → Click **New Merge Request** → Select Source Branch (`feature/user-auth`) and Target Branch (`main`).
   - On **GitHub:** Navigate to **Pull Requests** → Click **New Pull Request** → Select branches.
3. **Code Review & CI/CD Pipeline:** Team members review the code diff, leave comments, and automated CI tests run.
4. **Approval & Merge:** Once approved, the repository administrator clicks **Merge**, which merges the feature branch into `main` and optionallly deletes the remote branch.

### 3. P4Merge Visual Diff Tool Integration

To configure **P4Merge** as Git's default visual diff tool on Windows:

```bash
git config --global diff.tool p4merge
git config --global difftool.p4merge.path "C:/Program Files/Perforce/p4merge.exe"
git config --global difftool.prompt false
```

---

## 🧪 Hands-On Lab Walkthrough

### Part A: Branching Operations

#### 1. Create a New Branch `GitNewBranch`
Open Git Bash inside your project directory (`GitDemo`) and create the branch:
```bash
git branch GitNewBranch
```

#### 2. List Local and Remote Branches
List all available branches to verify creation:
```bash
git branch -a
```
**Output:**
```text
* main
  GitNewBranch
  remotes/origin/main
```
> **Note:** The asterisk `*` indicates the currently checked-out branch (`main`).

#### 3. Switch to `GitNewBranch`
Switch active branch working context to `GitNewBranch`:
```bash
git checkout GitNewBranch
```
*(Or in modern Git: `git switch GitNewBranch`)*

**Output:**
```text
Switched to branch 'GitNewBranch'
```

Verify branch pointer again:
```bash
git branch
```
**Output:**
```text
* GitNewBranch
  main
```

#### 4. Add Files & Content on `GitNewBranch`
Create a new file `feature.txt`:
```bash
echo "Feature Implementation on GitNewBranch" > feature.txt
echo "Added user authentication module." >> feature.txt
```

#### 5. Stage and Commit Changes to `GitNewBranch`
```bash
git add feature.txt
git commit -m "Add feature.txt with authentication logic on GitNewBranch"
```
**Output:**
```text
[GitNewBranch b2c3d4e] Add feature.txt with authentication logic on GitNewBranch
 1 file changed, 2 insertions(+)
 create mode 100644 feature.txt
```

#### 6. Check `git status`
```bash
git status
```
**Output:**
```text
On branch GitNewBranch
nothing to commit, working tree clean
```

---

### Part B: Merging Operations

#### 1. Switch Back to `main` (or `master`)
```bash
git checkout main
```
**Output:**
```text
Switched to branch 'main'
```

#### 2. View Command Line Differences (`git diff`)
Compare the differences between `main` and `GitNewBranch`:
```bash
git diff main..GitNewBranch
```
**CLI Output:**
```diff
diff --git a/feature.txt b/feature.txt
new file mode 100644
index 0000000..e69de29
--- /dev/null
+++ b/feature.txt
@@ -0,0 +1,2 @@
+Feature Implementation on GitNewBranch
+Added user authentication module.
```

#### 3. View Visual Differences using P4Merge
Launch P4Merge visual tool to compare branches:
```bash
git difftool main GitNewBranch
```
*(P4Merge GUI window opens displaying side-by-side file differences).*

#### 4. Merge `GitNewBranch` into `main`
Execute the merge command to incorporate branch changes into `main`:
```bash
git merge GitNewBranch -m "Merge branch 'GitNewBranch' into main"
```
**Output:**
```text
Updating a1b2c3d..b2c3d4e
Fast-forward
 feature.txt | 2 ++
 1 file changed, 2 insertions(+)
 create mode 100644 feature.txt
```

#### 5. Observe Log Graph After Merging
Display detailed graphical commit tree:
```bash
git log --oneline --graph --decorate --all
```
**Output:**
```text
* b2c3d4e (HEAD -> main, GitNewBranch) Add feature.txt with authentication logic on GitNewBranch
* a1b2c3d Initial commit: Add welcome.txt with introductory message
```

#### 6. Delete `GitNewBranch` and Check Status
Safe-delete the merged branch:
```bash
git branch -d GitNewBranch
```
**Output:**
```text
Deleted branch GitNewBranch (was b2c3d4e).
```

Verify branch list and repository status:
```bash
git branch -a
git status
```
**Output:**
```text
* main
  remotes/origin/main

On branch main
nothing to commit, working tree clean
```

---

## 📁 Lab Artifacts Included in this Folder

- [`GitDemo/feature.txt`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/3.%20Git-HOL/GitDemo/feature.txt) — Feature file created on `GitNewBranch` and merged into `main`.
- [`git_branching_merging_demo.sh`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/3.%20Git-HOL/git_branching_merging_demo.sh) — Shell script automating the entire lab workflow.
