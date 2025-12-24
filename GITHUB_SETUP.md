# How to Add This Project to GitHub

## Step 1: Initialize Git Repository
```bash
git init
```

## Step 2: Add All Files
```bash
git add .
```

## Step 3: Make Your First Commit
```bash
git commit -m "Initial commit: Decision Matrix Application"
```

## Step 4: Create a Repository on GitHub
1. Go to https://github.com and sign in
2. Click the "+" icon in the top right corner
3. Select "New repository"
4. Choose a repository name (e.g., "decision-matrix-app")
5. Choose Public or Private
6. **DO NOT** initialize with README, .gitignore, or license (we already have these)
7. Click "Create repository"

## Step 5: Connect Your Local Repository to GitHub
After creating the repository, GitHub will show you commands. Use these (replace YOUR_USERNAME and REPO_NAME):

```bash
git remote add origin https://github.com/YOUR_USERNAME/REPO_NAME.git
git branch -M main
git push -u origin main
```

## Alternative: If you already created a README on GitHub
If you accidentally initialized with a README, you'll need to pull first:
```bash
git remote add origin https://github.com/YOUR_USERNAME/REPO_NAME.git
git branch -M main
git pull origin main --allow-unrelated-histories
git push -u origin main
```

## That's it! 🎉
Your code is now on GitHub. Any future changes can be pushed with:
```bash
git add .
git commit -m "Your commit message"
git push
```

