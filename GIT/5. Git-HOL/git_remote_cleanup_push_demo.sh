#!/bin/bash
# ==============================================================================
# Git HOL 5: Remote Synchronization & Repository Cleanup Demonstration Script
# ==============================================================================

cd GitDemo || exit 1

echo "=========================================="
echo "Step 1: Verify master/main is in clean state"
echo "=========================================="
git status

echo ""
echo "=========================================="
echo "Step 2: List out all available branches"
echo "=========================================="
echo "Local and remote branches:"
git branch -a

echo ""
echo "=========================================="
echo "Step 3: Pull remote repository changes to master/main"
echo "=========================================="
git pull origin main 2>/dev/null || git pull origin master 2>/dev/null

echo ""
echo "=========================================="
echo "Step 4: Push pending local changes to remote repository"
echo "=========================================="
git push origin main 2>/dev/null || git push origin master 2>/dev/null

echo ""
echo "=========================================="
echo "Step 5: Observe and verify changes reflected in remote repository"
echo "=========================================="
echo "Latest commit log tree:"
git log --oneline --graph --decorate -n 5

echo ""
echo "Final working directory status:"
git status

echo ""
echo "=========================================="
echo "Git HOL 5 Execution Completed Successfully!"
echo "=========================================="
