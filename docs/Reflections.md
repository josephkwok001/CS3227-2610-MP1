
# Reflections on AI-assisted SE

This file contains Joseph's **deep dives** on AI-assisted software engineering for CS3227 MP1.

Per-increment prompt summaries and verified session logs live in [`logs/`](../logs/) (`01` through `17`).

Tools used: Cursor (agentic coding) + prompting.

## Guiding questions

Use these when expanding an increment into a deep dive:

- Why was the prompt formulated that way?
- What assumptions did the LLM make?
- What did it get wrong?
- How did I verify the result?
- How did the prompt evolve?
- When was prompting less effective than manual work?
- What engineering judgement was still required?
- What would I do differently next time?


## Deep dives

### Deep dive 1 — Chaining prompt strategies into a pipeline (ToT → checklist → few-shot CoT plan → zero-shot CoT implement)

For the money-formatting refactor (Increment 13) I didn't just throw one prompt at Cursor and accept whatever came back. I deliberately chained four prompting techniques into a pipeline, each stage feeding its output into the next:

1. **Tree-of-Thought (ToT) exploration.** I asked the model to lay out multiple different ways to implement centralised money formatting (e.g. a `MoneyFormatter` utility class vs. formatting logic inside `Expense`/`Income` vs. a `Formattable` interface), and to also generate a checklist of things a correct refactor must preserve (existing `find`/`summary` output, `BigDecimal` rounding, no behaviour change).
2. **Multi-shot Chain-of-Thought (CoT), plan-only.** I pasted the ToT output (branches + checklist) into a fresh prompt with a few worked planning examples, and explicitly told it to **only plan**, not write code yet. This forced it to commit to one branch and produce a numbered implementation plan before touching any file.
3. **Zero-shot CoT implementation.** I then pasted that numbered plan into a third, fresh chat and used the zero-shot COT prompting strategy "let's think step by step" to implement it. However I scoped it strictly to *implementing the plan as written* rather than re-deciding the design.

**Why I formulated it this way.** Doing design, planning, and implementation in one giant prompt tends to produce code where the "design" is implicit and hard to interrogate. If something goes wrong, I can't tell whether it was a design mistake or an implementation slip. Splitting it into three separate stages meant each stage had a narrow job, and I could inspect and approve the output of each stage independently before it became irreversible in code. However, one thing to note was that this strategy of prompting took quite a while, and pasting the output to a new chat was a hassle. Perhaps the tradeoff of this is that it is time consuming and requires multiple steps.

**What the LLM assumed / got wrong.** At the ToT stage it assumed I wanted backward-compatible output (I hadn't said this explicitly, but it inferred it from the existing tests) — that assumption turned out to be exactly right and probably saved me a round of rework. At the planning stage it initially proposed *merging* `Expense` and `Income` into one type as part of the refactor, which I explicitly rejected before moving to implementation, since that's a much bigger change than "fix formatting" and would have blown up the diff.

**How I verified.** `./gradlew check` stayed green, and because the plan explicitly said "no behaviour change," I could diff the test output before/after rather than re-reviewing everything from scratch.

**Engineering judgement that stayed with me.** The pipeline doesn't remove the need to make the actual design call (standalone class vs. shared type), it just makes *when* I make that call more deliberate. I still had to reject the Expense/Income merge myself; the pipeline surfaced it as an explicit decision point rather than letting it get silently folded into the implementation.

**Next time.** I'd keep the ToT-checklist artifact around as a lightweight design doc rather than discarding it. It would have been useful evidence in this very reflection, and it's the kind of thing that's easy to lose once you move to the next chat.

Session log: [`logs/13-refactor-money-formatting.md`](../logs/13-refactor-money-formatting.md).

### Deep dive 2 — Writing an `AGENTS.md` file to constrain the agent's own behaviour

In Increment 01 I asked Cursor to generate AGENTS.md and a matching Cursor rule (.cursor/rules/mp1-ai-workflow.mdc) so every session would inherit the same standing instructions: read Reflections.md first, implement one increment at a time, keep the User Guide accurate, write logs, and never add a Features.md.

**Why I did this.** Even with those files in place from the start, I still found myself re-stating the same constraints in feature prompts across increments 01–07: "only this increment," "don't touch reflections I've already written," "don't commit unless I ask." AGENTS.md absorbed the workflow rules, but it did not stop me from having to narrow scope in every session, and it did not stop the agent from making product decisions I should have owned.

**What was interesting about the output.** The first draft of AGENTS.md mixed two kinds of content. Useful but generic: SE-EDU Java style, Checkstyle, commit-message conventions. More valuable for this project: increment order, no to-do clone, logs after each session. The gap showed up clearly in Increment 06 (persist): the agent implemented data/budgie.txt and a Duke-style line format (E|amount|category|description) without me treating path or format as an explicit design choice first. That lesson landed in my increment reflection and in how I worded later prompts ("flag design choices before implementing"), but it never became its own line in AGENTS.md.

**Engineering judgement.** A constraint file is only as good as my enforcement of it. Cursor does not guarantee compliance, so in later increments I still spot-checked the first response against AGENTS.md (scope, UG sync) rather than assuming the agent had read and obeyed everything. The file reduced how often I had to explain the full workflow from scratch, it did not remove my responsibility to review diffs and catch silent design calls.

**What I'd do differently.** I would keep creating AGENTS.md in Increment 01, but I would update it immediately after Increment 06 with an explicit rule such as: do not choose persistence path or file format without flagging it as my decision. I spent several increments re-learning the same "don't overstep scope / don't silently decide architecture" lesson in prompts and reflections when a one-line addition to the standing file might have prevented the next occurrence.

Session log: [`logs/01-repo-setup-and-greet.md`](../logs/01-repo-setup-and-greet.md).

### Deep dive 3 — Tree-of-Thought for the GUI polish, picking B then course-correcting to C (Increment 17)

For the JavaFX GUI polish I used a ToT-style prompt asking Cursor to propose three distinct options rather than one: (A) CSS-only tweaks to the existing chat bubbles, (B) wider bubbles with better spacing, (C) a fuller `BorderPane` + `TextFlow` layout with a title bar and a dedicated input strip.

**Why I formulated it that way.** GUI polish is subjective and hard to specify precisely up front. I genuinely didn't know what was considered a good design until I could compare options side by side, so a single "make the GUI nicer" prompt would have given the model too much unconstrained freedom and me too little to react to. ToT let me evaluate concrete, bounded alternatives instead.

**What happened.** I approved and implemented Option B first, since not only did Cursor recommend I pick it, it also looked like the smallest and safest change. After running it and actually using the chat window, it didn't fix what I actually needed. The chat still felt cramped, the output text of the help feature was still messy. I then went back to the same ToT output and asked for Option C instead, which added the title bar, a distinct bottom input strip, and `TextFlow` bubbles. This version addressed the actual usability issue.

**What did it get wrong / what did I get wrong?** In hindsight, the LLM didn't get anything wrong here. *I* picked the option based on how it read in the plan description rather than how it would feel to actually use, which is exactly the kind of thing a written description under-specifies. The model's B and C implementations both matched their descriptions faithfully; the mismatch was between my mental model and what I actually needed.

**How I verified.** `./gradlew check` stayed green for both attempts (no GUI tests exist by design), so verification here was manual: running the app and using the chat window myself, which is what actually caught B's shortfall — a purely automated check would not have.

**Engineering judgement / what I'd do differently.** I should have picked one option and fully committed to evaluating it interactively *before* asking for the next, rather than assuming I could judge from the plan text alone. Especially since this was a section on the UI, where the LLM may not be able to fully capture what I envision. More generally, for any UI/UX decision, ToT's value comes from generating options, but *choosing* the right option still requires trying it, not just reading about it. Next time I'd budget for "try it, then maybe switch" as part of the ToT workflow rather than treating my first pick as final.

Session log: [`logs/17-gui-polish-and-help-format.md`](../logs/17-gui-polish-and-help-format.md).

### Deep dive 4 — AI-assisted unit test generation surfaced a real gap, but also duplicated existing coverage (Increment 14)

For Increment 14 I followed the lecture's structured approach to AI-assisted unit testing, where I defined a specific scope/function. However, I also shaped the prompts around the starter prompt introduced in lecture and introduced some rules that I wanted to try. For example act like an autonomous software engineer, optimise for correctness over speed, conform to existing conventions, search before duplicating, and only claim completion after reconciling the plan.

**Why I formulated the prompts that way.** I prepended a shared rules block to every prompt. I told it tests only, no production changes unless a real bug appears. I also told it to frequently run ./gradlew check and explain each case in a scenario table linked to the User Guide. Then I sent four separate, narrowly scoped prompts: EntryMatcher, expanded MoneyFormatterTest, ExpenseBook.delete, and consolidated Parser amount validation, each ending with "STOP after one test class + explanation."

Autonomy and persistence (bounded). Default agent instructions say something like "once the user gives a direction, proactively gather context, plan, implement, test, and refine without waiting at every step." I wanted that autonomy for implementation, but not for scope. One broad "write more tests" prompt would let the agent roam across the whole suite and make review painful. Splitting into four prompts gave me bounded autonomy: the agent could still read files, generate tests, and iterate on Checkstyle within one class, but I could revert or reject one chunk without untangling four concerns. Prompt 1 even said "Do not duplicate scenarios already covered in FindCommandTest unless they test EntryMatcher directly" — an attempt to steer the agent toward DRY / search first before adding new @Test methods.


**What the LLM got right.** It found a genuine gap I had missed: income amounts with more than two decimal places (e.g. 12.555) were not covered anywhere, even though the User Guide says they should be rejected. That came from building a full scenario table for Parser amount validation — a good outcome of letting it plan cases systematically rather than me listing every edge case by hand.

**What it got wrong.** Several EntryMatcher and ExpenseBook tests duplicated scenarios already in FindCommandTest and DeleteCommandTest. The agent had been told to search for overlap, but it did not treat "read existing test files and skip covered scenarios" as part of search first, it generated technically correct but redundant tests. On Prompt 4, its first pass failed Checkstyle despite the shared block saying to run ./gradlew check. This was actually the opposite of what I wanted it to do, which was to "optimise for correctness and reliability over speed" and "conform to codebase conventions". ./gradlew check only went green after I asked it to fix Checkstyle. It had implemented tests but had not reconciled "green build" as a stated intention before ending the turn.


**How I verified.** I ran ./gradlew check myself after each prompt, skimmed new tests against the existing suite for duplication (nothing automated flags redundant-but-passing tests), and used the agent's scenario tables to decide which cases were net-new vs already covered at the command layer.

**Engineering judgement that stayed with me.** This is the clearest example of the lecture's "review, refine, and consolidate" step mattering. Accepting every generated @Test would bloat the suite without improving coverage. I also learned that autonomy needs explicit guardrails: "search existing tests first," "run check before saying done," and "mark blocked items (e.g. Checkstyle) instead of implying completion." I kept some duplicate model-layer tests for refactor safety, but I am not fully happy with that trade-off.


**Next time.** I would add explicit prompt lines drawn from those agent rules:
- (1) DRY: "List existing tests that cover each scenario; only add a test if no prior art exists or this layer must be tested in isolation."
- (2) Closure: "Do not end until ./gradlew check is green; if Checkstyle fails, fix and re-run before summarising."
- (3) Bounded autonomy: keep one-class-per-prompt, but paste paths to FindCommandTest / DeleteCommandTest so search-first is concrete, not implied.

Session log: [`logs/14-ai-unit-tests.md`](../logs/14-ai-unit-tests.md).
