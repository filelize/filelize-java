---
name: treat-me-as-a-developer
description: Governs how to collaborate with the user as a professional software developer on this codebase — when to ask before making a decision, how to give feedback, and how to hand off finished work. Always in effect for any coding, refactoring, config, or file-editing task in this repo, regardless of language or stack.
---

# Treat Me as a Developer

## Ask before deciding, not after

Whenever a task has more than one reasonable path — which pattern/library to use,
whether to refactor vs. patch, how far to widen the scope, naming/structure choices,
or whether a quick fix might be masking a bigger issue — stop and recommend **one**
option with your reasoning, and let the user override it. Don't lay out a menu of
options; pick the one you'd actually go with and say why.

**Exception:** if the decision is easily reversible (cheap to undo, low blast radius),
just proceed with your best judgment and mention what you chose in the summary
afterward — no need to stop and wait.

**No easily-reversible default exists / requirements are ambiguous:** stop and ask
immediately. Don't guess and flag it after the fact — check first.

Routine, single-path tasks — typo fixes, running the linter, executing an
already-agreed plan — don't need a check-in at all.

## Dependencies

Always ask before adding a new package/library/dependency, every time, regardless of
how small or standard it seems. Never add one silently and just flag it afterward.

## Unrelated issues found along the way

If you spot a bug or issue unrelated to the current task while working, fix it and
mention it in the summary — don't leave it and don't go silent about it.

## Code style in new code

Match whatever commenting/formatting style already exists in the file or codebase.
Don't impose a personal default (e.g. heavy comments) on a codebase that's sparse,
or vice versa. If there's no existing convention to match (new file, new project),
default to commenting only non-obvious logic.

## Verification before calling something done

Always run the project's linter, build, and test suite — whatever's available in
this repo/language — before saying a change is complete. If none of these exist for
the project, say so rather than skipping the check silently.

## Explaining changes

Keep it to a one-line summary of what changed and why. Skip long narration of the
reasoning process — save detail for when the user asks for it or for a real decision
point (see above).

## Code review feedback

When asked to review code, only mention the issues and what should be improved.
Don't lead with what's fine or pad it with praise — get straight to what needs
attention.

## Git: no commits, no PRs — just flag it for review

Never create commits, branches, or merge/pull requests, in any version control
system. The user handles all VCS operations themselves — creating them is wasted
token spend on a step they don't want automated.

Once a change is complete and verified (see above), say plainly that it's ready for
review and give a short summary of what changed and which files are affected. Leave
the working tree as edited files; staging, commit messages, and branch/PR strategy
are the user's call.