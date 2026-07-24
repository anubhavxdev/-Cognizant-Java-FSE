#!/bin/bash
# ==============================================================================
# Git HOL 3: Branching & Merging Demonstration Script
# ==============================================================================

echo "=========================================="
echo "Part 1: Branching Operations"
echo "=========================================="

cd GitDemo || exit 1

echo "1. Creating new branch 'GitNewBranch'..."
git branch GitNewBranch

echo ""
echo "2. Listing all local and remote branches:"
git branch -a

echo ""
echo "3. Switching to 'GitNewBranch'..."
git checkout GitNewBranch

echo ""
echo "Verifying current branch pointer (* denotes active branch):"
git branch

echo ""
echo "Creating feature.txt on GitNewBranch..."
cat << 'EOF' > feature.txt
Feature Implementation on GitNewBranch
This file was created on GitNewBranch and merged into master/main.
Added feature details: User Authentication & Feature Toggle logic.
EOF

echo ""
echo "4. Staging and committing changes on 'GitNewBranch'..."
git add feature.txt
git commit -m "Add feature.txt with authentication logic on GitNewBranch"

echo ""
echo "5. Checking status on GitNewBranch:"
git status

echo ""
echo "=========================================="
echo "Part 2: Merging Operations"
echo "=========================================="

echo "1. Switching back to main/master..."
git checkout main 2>/dev/null || git checkout master 2>/dev/null

echo ""
echo "2. Viewing differences between main and GitNewBranch (git diff):"
git diff main..GitNewBranch 2>/dev/null || git diff master..GitNewBranch 2>/dev/null

echo ""
echo "3. P4Merge diff tool integration command:"
echo "   Command: git difftool main GitNewBranch"

echo ""
echo "4. Merging 'GitNewBranch' into active branch..."
git merge GitNewBranch -m "Merge branch 'GitNewBranch' into main"

echo ""
echo "5. Viewing log graph after merge (git log --oneline --graph --decorate):"
git log --oneline --graph --decorate -n 5

echo ""
echo "6. Deleting merged branch 'GitNewBranch'..."
git branch -d GitNewBranch

echo ""
echo "Verifying branch list after deletion:"
git branch -a

echo ""
echo "Final git status:"
git status

echo ""
echo "=========================================="
echo "Git HOL 3 Execution Completed Successfully!"
echo "=========================================="
