# Lab 4: Merge Conflict Resolution & 3-Way Merge Tools

> **Module:** 4. GIT-HOL  
> **Estimated Time:** 30 Minutes  
> **Prerequisites:** Hands-on ID "Git-T03-HOL_001", Git environment, P4Merge / 3-way merge tool configured  
> **Objectives:**  
> • Understand how and why merge conflicts occur when multiple developers edit the same file regions.  
> • Inspect and interpret Git conflict marker syntax (`<<<<<<<`, `=======`, `>>>>>>>`).  
> • Use 3-way merge tools (P4Merge) to resolve conflicts.  
> • Handle backup files (`*.orig`) using `.gitignore` and safely complete the merge.

---

## 📖 Theory & Core Concepts

### What Causes a Merge Conflict?
A merge conflict occurs when Git attempts to merge two branches that have **divergent commits modifying the exact same lines of a file** differently. Because Git cannot automatically determine which developer's version is correct, it halts the merge process and places **conflict markers** inside the affected file.

### Git Conflict Marker Syntax
When a conflict occurs, Git modifies the file to show both competing versions:

```xml
<<<<<<< HEAD (Current branch, e.g., master)
<setting name="ApplicationName" value="GitDemo Master" />
<setting name="MaxConnections" value="100" />
=======
<setting name="ApplicationName" value="GitDemo Branch" />
<setting name="Timeout" value="3000" />
>>>>>>> GitWork (Incoming branch being merged)
```

- `<<<<<<< HEAD`: Marks the start of the conflict block from your current checked-out branch.
- `=======`: Separates your local changes from the incoming branch changes.
- `>>>>>>> GitWork`: Marks the end of the conflict block from the incoming branch.

### 3-Way Merge & P4Merge Tool
A 3-way merge tool compares three versions of a file:
1. **BASE:** The common ancestor commit before the branches diverged.
2. `LOCAL` (HEAD / Ours): The file state on your current target branch (`master`).
3. `REMOTE` (Theirs / Incoming): The file state on the branch being merged (`GitWork`).

To launch **P4Merge** for 3-way conflict resolution:
```bash
git mergetool
```

> **Note on Backup Files (`*.orig`):** Many mergetools create a `.orig` backup file (e.g., `hello.xml.orig`) containing the unmerged file with conflict markers. These must be ignored using `.gitignore` or disabled via `git config --global mergetool.keepBackup false`.

---

## 🧪 Hands-On Lab Walkthrough (Steps 1–19)

### Step 1: Verify `master` is in a Clean State
Navigate to `GitDemo` and ensure no uncommitted changes exist:
```bash
git status
```
**Output:**
```text
On branch master (or main)
nothing to commit, working tree clean
```

---

### Step 2–4: Create Branch `GitWork`, Add `hello.xml`, and Commit
Create and switch to branch `GitWork`:
```bash
git checkout -b GitWork
```

Create `hello.xml` with branch-specific configuration:
```bash
cat << 'EOF' > hello.xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appSettings>
        <setting name="ApplicationName" value="GitDemo Branch" />
        <setting name="Timeout" value="3000" />
    </appSettings>
</configuration>
EOF
```

Stage and commit changes on `GitWork`:
```bash
git add hello.xml
git status
git commit -m "Add hello.xml on GitWork branch with branch settings"
```
**Output:**
```text
[GitWork c3d4e5f] Add hello.xml on GitWork branch with branch settings
 1 file changed, 7 insertions(+)
 create mode 100644 hello.xml
```

---

### Step 5–7: Switch to `master`, Add Conflicting `hello.xml`, and Commit
Switch back to `master`:
```bash
git checkout master
```

Create `hello.xml` on `master` with **different** content for the same settings:
```bash
cat << 'EOF' > hello.xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appSettings>
        <setting name="ApplicationName" value="GitDemo Master" />
        <setting name="MaxConnections" value="100" />
    </appSettings>
</configuration>
EOF
```

Stage and commit changes on `master`:
```bash
git add hello.xml
git commit -m "Add conflicting hello.xml on master branch"
```
**Output:**
```text
[master d4e5f6a] Add conflicting hello.xml on master branch
 1 file changed, 7 insertions(+)
 create mode 100644 hello.xml
```

---

### Step 8: Observe Log Graph
Inspect divergent branch history before merging:
```bash
git log --oneline --graph --decorate --all
```
**Output:**
```text
* d4e5f6a (HEAD -> master) Add conflicting hello.xml on master branch
| * c3d4e5f (GitWork) Add hello.xml on GitWork branch with branch settings
|/
* b2c3d4e Add feature.txt with authentication logic on GitNewBranch
```

---

### Step 9–10: Diff Branches CLI & Visual P4Merge Tool
Check CLI differences:
```bash
git diff master..GitWork
```
Launch visual diff tool (P4Merge):
```bash
git difftool master GitWork
```

---

### Step 11–12: Attempt Merge & Observe Git Conflict Markup
Merge `GitWork` into `master`:
```bash
git merge GitWork
```
**Output:**
```text
Auto-merging hello.xml
CONFLICT (add/add): Merge conflict in hello.xml
Automatic merge failed; fix conflicts and then commit the result.
```

Inspect the conflict markers placed inside `hello.xml`:
```bash
cat hello.xml
```
**Output:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appSettings>
<<<<<<< HEAD
        <setting name="ApplicationName" value="GitDemo Master" />
        <setting name="MaxConnections" value="100" />
=======
        <setting name="ApplicationName" value="GitDemo Branch" />
        <setting name="Timeout" value="3000" />
>>>>>>> GitWork
    </appSettings>
</configuration>
```

---

### Step 13–14: Resolve Conflict using 3-Way Merge Tool & Commit
Launch 3-way merge tool (P4Merge) to resolve:
```bash
git mergetool
```
*Or manually edit `hello.xml` to integrate both settings:*

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appSettings>
        <!-- Conflict resolved: Integrated both master and GitWork configurations -->
        <setting name="ApplicationName" value="GitDemo Enterprise" />
        <setting name="Environment" value="Production" />
        <setting name="MaxConnections" value="100" />
        <setting name="Timeout" value="3000" />
        <setting name="EnableLogging" value="true" />
    </appSettings>
</configuration>
```

Stage resolved file and commit:
```bash
git add hello.xml
git commit -m "Merge branch 'GitWork': Resolve merge conflict in hello.xml"
```
**Output:**
```text
[master e5f6a7b] Merge branch 'GitWork': Resolve merge conflict in hello.xml
```

---

### Step 15–16: Handle Mergetool Backup Files (`*.orig`) in `.gitignore`
After mergetool execution, check `git status`:
```bash
git status
```
If backup files (e.g. `hello.xml.orig`) are listed as untracked, update `.gitignore`:
```bash
echo "*.orig" >> .gitignore
git add .gitignore
git commit -m "Add *.orig backup pattern to .gitignore"
```

---

### Step 17–19: Delete Branch & Verify Clean Graph Log
List branches:
```bash
git branch -a
```

Delete the merged branch `GitWork`:
```bash
git branch -d GitWork
```
**Output:**
```text
Deleted branch GitWork (was c3d4e5f).
```

Observe final commit log graph:
```bash
git log --oneline --graph --decorate
```
**Output:**
```text
* e5f6a7b (HEAD -> master) Merge branch 'GitWork': Resolve merge conflict in hello.xml
|\  
| * c3d4e5f Add hello.xml on GitWork branch with branch settings
* | d4e5f6a Add conflicting hello.xml on master branch
|/  
* b2c3d4e Add feature.txt with authentication logic on GitNewBranch
```

---

## 📁 Lab Artifacts Included in this Folder

- [`GitDemo/hello.xml`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/4.%20Git-HOL/GitDemo/hello.xml) — Resolved `hello.xml` file integrating changes from both branches.
- [`GitDemo/.gitignore`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/4.%20Git-HOL/GitDemo/.gitignore) — `.gitignore` updated with `*.orig` pattern.
- [`git_conflict_resolution_demo.sh`](file:///c:/Users/anubh/OneDrive/Desktop/kranti/cognizant/myassests/-Cognizant-Java-FSE/GIT/4.%20Git-HOL/git_conflict_resolution_demo.sh) — Shell script automating conflict simulation, resolution, and cleanup.
