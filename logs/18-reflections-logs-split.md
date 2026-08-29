# Log 18 — Split increment journal into logs; Reflections deep dives only

**Date:** 29 Aug 2026  
**Thread intent:** Reorganise submission docs so `docs/Reflections.md` holds only deep dives (≥3 detailed prompt examples) and per-increment notes live in `logs/01`–`17` under **Reflection notes** sections.  
**Verification:** Docs only. Joseph should skim one log and Reflections.md to confirm nothing was lost.

---

## Prompts (summary)

1. User asked to move increment journal content (increments 01–17) from Reflections into corresponding log files; keep Reflections for deep dives only.

## What was produced

- **`docs/Reflections.md`:** Removed increment journal (~240 lines). Kept intro, guiding questions, 4 deep dives; added links to matching log files.
- **`logs/01`–`17`:** Added **Reflection notes** section to each file (merged from former increment journal).
- **`logs/README.md`:** Points to Reflections deep dives vs per-log reflection notes.
- **`AGENTS.md`**, **`.cursor/rules/mp1-ai-workflow.mdc`**, **`docs/DeveloperGuide.md`:** Workflow updated (logs only, no increment stubs in Reflections).

## Files touched

- `docs/Reflections.md`
- `logs/README.md`, `logs/01` through `logs/17`
- `AGENTS.md`, `.cursor/rules/mp1-ai-workflow.mdc`, `docs/DeveloperGuide.md`
- `logs/18-reflections-logs-split.md` (this file)

## Checks

- Intended: `./gradlew check` (no code changes expected to affect tests)
- Joseph should verify deep dive count ≥3 and logs/ still has 17 increment files

## Reflection notes

The course asks for both a reflection document (≥3 detailed prompt examples) and a logs folder (all interactions). Keeping both in Reflections duplicated the logs. Splitting matches the submission layout better: logs are the chronological archive; Reflections is the curated analysis (deep dives 1–4 on ToT pipeline, AGENTS.md, GUI ToT, AI unit tests).

Joseph should still rewrite reflection notes in each log in first person if any sections read like agent drafts.
