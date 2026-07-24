#!/bin/bash
# ==============================================================================
# Git HOL 1: Git Configuration & Basic Operations Automation Script
# ==============================================================================

echo "=========================================="
echo "Step 1: Check Git Installation & Config"
echo "=========================================="
git --version

echo ""
echo "Configuring user name and email..."
git config --global user.name "Learner Name"
git config --global user.email "learner@example.com"

echo ""
echo "Current Git Configuration:"
git config --global --list

echo ""
echo "=========================================="
echo "Step 2: Configure Default Editor (Notepad++)"
echo "=========================================="
git config --global core.editor "'C:/Program Files/Notepad++/notepad++.exe' -multiInst -notabbar -nosession -noPlugin"
echo "Default editor configured:"
git config --global core.editor

echo ""
echo "=========================================="
echo "Step 3: Create & Track Repository (GitDemo)"
echo "=========================================="
mkdir -p GitDemo
cd GitDemo

echo "Initializing Git repository..."
git init

echo "Creating welcome.txt..."
echo "Welcome to Git Hands-On Learning!" > welcome.txt
echo "This is a sample file created for Git HOL 1." >> welcome.txt
echo "Git configuration and repository setup completed successfully." >> welcome.txt

echo ""
echo "Verifying file content:"
cat welcome.txt

echo ""
echo "Checking git status (untracked file):"
git status

echo ""
echo "Staging welcome.txt..."
git add welcome.txt

echo ""
echo "Checking git status (staged file):"
git status

echo ""
echo "Committing welcome.txt..."
git commit -m "Initial commit: Add welcome.txt with initial welcome message"

echo ""
echo "Checking git log:"
git log --oneline

echo ""
echo "=========================================="
echo "Lab 1 Setup & Execution Completed!"
echo "=========================================="
