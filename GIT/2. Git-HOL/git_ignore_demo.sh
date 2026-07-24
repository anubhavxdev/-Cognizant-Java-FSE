#!/bin/bash
# ==============================================================================
# Git HOL 2: Git Ignore & Unwanted Files Management Demonstration Script
# ==============================================================================

echo "=========================================="
echo "Step 1: Setup Local Repository & Add Source Files"
echo "=========================================="
cd GitDemo || exit 1

echo "Creating source file app.py..."
cat << 'EOF' > app.py
# Sample Source Code File
def main():
    print("Application started successfully.")

if __name__ == "__main__":
    main()
EOF

echo ""
echo "Creating log file (app.log) and log folder (logs/debug.log)..."
mkdir -p logs
echo "[2026-07-24 14:30:00] INFO: Application started." > app.log
echo "[2026-07-24 14:30:00] DEBUG: Initializing log folder..." > logs/debug.log

echo ""
echo "=========================================="
echo "Step 2: Check Git Status WITHOUT .gitignore"
echo "=========================================="
# Temporarily rename .gitignore to demonstrate untracked status
if [ -f .gitignore ]; then
    mv .gitignore .gitignore.bak
fi

echo "Git status before adding .gitignore:"
git status

echo ""
echo "=========================================="
echo "Step 3: Create & Apply .gitignore"
echo "=========================================="
if [ -f .gitignore.bak ]; then
    mv .gitignore.bak .gitignore
else
    cat << 'EOF' > .gitignore
# Ignore all files with .log extension
*.log

# Ignore log directories
logs/
log/

# Ignore temporary files
*.tmp
EOF
fi

echo "Contents of .gitignore:"
cat .gitignore

echo ""
echo "=========================================="
echo "Step 4: Verify Git Status WITH .gitignore"
echo "=========================================="
echo "Git status after applying .gitignore (app.log and logs/ MUST be ignored):"
git status

echo ""
echo "=========================================="
echo "Step 5: Verify File Tracking (git check-ignore)"
echo "=========================================="
echo "Checking if app.log is ignored:"
git check-ignore -v app.log

echo "Checking if logs/debug.log is ignored:"
git check-ignore -v logs/debug.log

echo ""
echo "=========================================="
echo "Step 6: Stage and Commit Tracked Files"
echo "=========================================="
git add .
git status

git commit -m "Add source file app.py and .gitignore rules for logs"

echo ""
echo "Git Log History:"
git log --oneline

echo ""
echo "=========================================="
echo "Git HOL 2 Execution Completed Successfully!"
echo "=========================================="
