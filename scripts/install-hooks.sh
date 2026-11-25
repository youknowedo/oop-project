#!/usr/bin/env bash
# Points git at the tracked hooks in .githooks/. Run once per clone.
set -euo pipefail
cd "$(git rev-parse --show-toplevel)"
chmod +x .githooks/*
git config core.hooksPath .githooks
echo "Git hooks enabled (core.hooksPath=.githooks)."
