#!/bin/bash
# ==============================================================================
# Git HOL 4: Merge Conflict Resolution Demonstration Script
# ==============================================================================

cd GitDemo || exit 1

echo "=========================================="
echo "Step 1: Verify master is in clean state"
echo "=========================================="
git status

echo ""
echo "=========================================="
echo "Step 2-4: Create branch 'GitWork', add hello.xml & commit"
echo "=========================================="
git checkout -b GitWork

cat << 'EOF' > hello.xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appSettings>
        <setting name="ApplicationName" value="GitDemo Branch" />
        <setting name="Timeout" value="3000" />
    </appSettings>
</configuration>
EOF

git add hello.xml
git status
git commit -m "Add hello.xml on GitWork branch with branch settings"

echo ""
echo "=========================================="
echo "Step 5-7: Switch to master, create conflicting hello.xml & commit"
echo "=========================================="
git checkout main 2>/dev/null || git checkout master 2>/dev/null

cat << 'EOF' > hello.xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appSettings>
        <setting name="ApplicationName" value="GitDemo Master" />
        <setting name="MaxConnections" value="100" />
    </appSettings>
</configuration>
EOF

git add hello.xml
git commit -m "Add conflicting hello.xml on master branch"

echo ""
echo "=========================================="
echo "Step 8: Observe graph log"
echo "=========================================="
git log --oneline --graph --decorate --all -n 5

echo ""
echo "=========================================="
echo "Step 9-10: Check differences with git diff and P4Merge"
echo "=========================================="
echo "Diff output between master and GitWork:"
git diff main..GitWork 2>/dev/null || git diff master..GitWork 2>/dev/null

echo ""
echo "=========================================="
echo "Step 11-12: Trigger merge conflict"
echo "=========================================="
echo "Attempting to merge GitWork into master..."
git merge GitWork

echo ""
echo "Conflict detected! Inspected Git conflict markup in hello.xml:"
cat hello.xml

echo ""
echo "=========================================="
echo "Step 13-14: Resolve conflict and commit"
echo "=========================================="
cat << 'EOF' > hello.xml
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
EOF

# Create a mock P4Merge backup file hello.xml.orig to demonstrate step 15
touch hello.xml.orig

git add hello.xml
git commit -m "Merge branch 'GitWork': Resolve merge conflict in hello.xml"

echo ""
echo "=========================================="
echo "Step 15-16: Add *.orig backup file to .gitignore & commit"
echo "=========================================="
echo "Git status before ignoring backup file:"
git status

if ! grep -q "\*.orig" .gitignore 2>/dev/null; then
    echo "*.orig" >> .gitignore
fi

git add .gitignore
git commit -m "Add *.orig backup pattern to .gitignore"

echo ""
echo "=========================================="
echo "Step 17-19: Delete merged branch and inspect log graph"
echo "=========================================="
git branch -d GitWork

echo "Remaining branches:"
git branch -a

echo ""
echo "Log graph after merge conflict resolution:"
git log --oneline --graph --decorate -n 6

echo ""
echo "=========================================="
echo "Git HOL 4 Execution Completed Successfully!"
echo "=========================================="
