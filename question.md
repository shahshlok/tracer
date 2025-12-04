# Critical Design Questions: Persona-Guided Generation

Before we commit to a plan, we need to resolve these questions. This is the difference between a thesis that gets into ITiCSE and one that gets desk-rejected.

---

## The Core Tension

You want:
1. **Realistic, messy student code** (not surgically injected bugs)
2. **Known ground truth** (so you can measure precision/recall)

These are in tension. The more "natural" you make generation, the less certain you are about what misconception is present.

---

## Question 1: How do we validate the generated code has the intended misconception?

**Current approach:** Explicit instruction ("DO NOT use Scanner, hardcode values") → guaranteed the misconception is present.

**Persona approach:** Describe a student who "doesn't understand that programs should work for any input" → LLM generates code → **but did it actually produce the Concrete Instance Fallacy?**

Options:
- **(A) Trust the LLM**: Generate and assume it worked. Risk: ground truth is wrong, metrics are meaningless.
- **(B) Human validation**: Generate, then manually check each file. Risk: time-consuming, doesn't scale.
- **(C) Automated validation**: Write validators for each misconception (e.g., regex for "no Scanner import" + "hardcoded literals"). Risk: building 15+ validators.
- **(D) Hybrid generation**: Persona sets the style/noise, but still include a soft constraint like "this student tends to hardcode values from examples". Risk: still somewhat surgical.

**My recommendation:** Option D. Personas guide style and noise; a soft misconception hint guides the bug. Then spot-check 10-20% manually.

**Your answer:**
yeah i think option D is the best. How about we create something deterministic so that we can test if LLMs can actually detect the misconceptions as opposed to worrying about if it can generate submissions that are realistic for a CS1 course?

For now, the goal is misconception detection. We can definitely include the generation aspect in the future.
---

## Question 2: What's the actual contribution for ITiCSE?

ITiCSE cares about novelty AND rigor. What's the paper about?

- **(A) "LLMs can detect CS1 misconceptions"** — weak, everyone suspects this
- **(B) "Here's a methodology for evaluating misconception detection"** — the hybrid matcher, metrics, framework
- **(C) "Persona-driven synthetic student generation for CS education research"** — novel data generation method
- **(D) "Comparing prompting strategies for misconception detection"** — the baseline vs minimal vs rubric vs socratic comparison

If you want to do (C), that's a different paper. The generation method becomes the contribution, and detection becomes validation that the method works.

If you want to do (B) or (D), keep generation simple and focus on the detection evaluation.

**You cannot do both well in one thesis/paper.**

**Your answer:**
Lets do B. I want us to design a framework for evaluating misconception detection. We can include the generation aspect in the future. The framewor that we have right now is really good. we can add more llm or other deep learning modesl to make our evaluations more rigorous, something that the research community would find interesting.
---

## Question 3: Is 60 students × 4 questions enough for ITiCSE?

With the new distribution (35% seeded):
- 240 files total
- ~84 seeded files
- ~156 clean files

For statistical power on precision/recall with bootstrap CIs, this is borderline acceptable. ITiCSE reviewers might ask "why only 60 students?"

Options:
- Keep 60 students, defend with "controlled study, quality over quantity"
- Scale to 100-120 students (trivial with synthetic generation)
- Add a "replication" dimension: generate 2-3 different seeds and show consistency

**Your answer:**
I think we should keep it at 60 students. We can always scale it up in the future. The goal is to show that our framework works. We can always add more students in the future. Plus I am paying out of my own pocket so I don't want to spend too much money without knowing that our approach shows promise.

Once we have a something to show that our framework works, we can always scale it up and add that replication dimension too.
---

## Question 4: How subtle should the misconceptions be?

Your current groundtruth has misconceptions like:
- `STA-02 Concrete Instance Fallacy` — very obvious (no Scanner at all)
- `TYP-01 Integer Division Trap` — subtle (code looks right but wrong type)
- `ALG-01 Precedence Blindness` — subtle (missing parentheses)

If you make generation "messier," the subtle ones become nearly impossible to detect. That's realistic, but your recall will tank.

Options:
- **(A) Keep all misconceptions, accept low recall on subtle ones** — honest, but weak results
- **(B) Focus on 5-6 "detectable" misconceptions** — narrower scope, stronger results
- **(C) Report results stratified by misconception "detectability"** — nuanced, shows where LLMs struggle

**Your answer:**
I think Option A makes more sense to be honest. we can have 5-6 misconceptions that arent subtle enough to tank our recall so that we can get strong results. What do you think? i need more inputs on this from you.
---

## Question 5: Do we need to re-run everything?

If we change the dataset, we need to:
1. Regenerate manifest
2. Regenerate all student Java files (API calls to GPT)
3. Re-run detection across all strategies/models (API calls)
4. Re-run analysis

Estimated cost: ~$20-50 in API calls, 2-4 hours of compute time.

Are you prepared for this?

**Your answer:**
I am ready if it means we actually produce something that shows promise- iticse ready
---

## My Honest Take

For an ITiCSE-quality paper, I would:

1. **Keep the contribution focused on evaluation methodology** (hybrid matcher, metrics framework)
2. **Use "soft-guided" generation** (persona + hint, not surgical instructions)
3. **Fix the distribution** (35% seeded, realistic per-student rates)
4. **Scale to 100 students** (cheap with synthetic data)
5. **Validate 20% manually** and report inter-rater reliability if possible
6. **Be explicit about synthetic data as a limitation** but frame it as "controlled evaluation"

This is achievable in ~1 week of work without scrapping what you have.

---

## Next Step

Answer the questions above. Once I know your priorities, I'll write a concrete `plan.md`.
