---
trigger: always_on
description: CodeGraph usage rules for exploring and modifying the codebase
---

# CodeGraph Usage Rules

## Before Implementation
- Always use CodeGraph to understand existing code before implementing new features.
- Use `codegraph_explore` to investigate related classes, functions, dependencies and data flows.
- Identify reusable components and existing implementations.
- Do not assume the project architecture without inspecting the codebase.

## During Implementation
- Follow existing architecture and coding conventions.
- Reuse existing classes, components and utilities whenever possible.
- Avoid duplicated business logic.
- Check dependencies before modifying shared components.

## After Implementation
- Use CodeGraph to analyze the impact of modified classes and functions.
- Identify potentially affected features and call paths.
- Check relevant tests and update them when necessary.
- Read source files directly when CodeGraph reports pending or stale changes.

## Important
- Do not implement code immediately when requirements are unclear.
- Analyze first, propose a solution, then implement.
- Do not introduce unnecessary architectural changes.
