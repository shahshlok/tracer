# Research Questions & Thesis

## Thesis Statement

> "We define the **'Notional Machine Gap'** in Large Language Models. Through a controlled ablation study across increasing state complexity (Scalar → Temporal → Spatial → Heap), we demonstrate that LLM diagnostic capability is inversely proportional to the abstraction level of the Notional Machine. While LLMs achieve near-perfect recall on Syntax and API misconceptions, they exhibit a 'Diagnostic Ceiling' on mental models involving state and sequence, often failing to distinguish between student intent and valid execution flow. Furthermore, we identify a **'Prompting Paradox'**: mechanical prompting strategies (e.g., Chain-of-Thought) significantly outperform pedagogical role-playing (e.g., Socratic tutors), suggesting that 'sounding like a teacher' inhibits the model's ability to 'see like a machine'."

---

## Primary Research Questions

### RQ1. The Notional Machine Hierarchy (formerly Diagnostic Ceiling)

**Question:** Does LLM diagnostic performance degrade predictably as the **Notional Machine** complexity increases?

**Hypothesis:** We hypothesize a distinct performance tier list:

1.  **Scalar State (High Performance):** Simple variable assignment (`A1`).
2.  **Temporal State (Medium Performance):** Control flow and loops (`A2`).
3.  **Spatial/Referential State (Low Performance):** Arrays and Objects (`A3/A4`).

- **Goal:** To prove that LLMs are "State Blind"—they struggle to track data lifecycles across time (loops) and space (memory heaps).

### RQ2. Failure Boundaries (The "Invisible" Misconceptions)

**Question:** Which specific **Mental Models** are invisible to LLMs?

**Explanation:**
We classify errors not by syntax, but by the flawed _Notional Machine_ the student holds. We investigate detection rates for:

- **The Reactive State Machine:** Believing variables update automatically (Spreadsheet View).
- **The Anthropomorphic I/O:** Believing the computer "understands" prompts pragmatically.
- **The Teleological Flow:** Believing code executes based on "intent" rather than strict sequence.
- **Goal:** To create a taxonomy of errors that are "safe" for AI to grade vs. those requiring human intervention.

### RQ3. The Prompting Paradox

**Question:** Does **Pedagogical Role-Playing** (Socratic Method) degrade diagnostic accuracy compared to **Mechanical Simulation** (Chain-of-Thought)?

**Explanation:**
Instructional design suggests tutors should focus on the student's "Mental Model." However, asking an LLM to "act as a tutor" may introduce linguistic noise or hallucinated empathy.

- **Comparison:** We test `Zero-Shot Baseline` vs. `Taxonomy-Primed` vs. `Chain-of-Thought (Trace)` vs. `Socratic Persona`.
- **Goal:** To prove that the best way to make an LLM a good teacher is to force it to act like a rigid runtime environment first.

### RQ4. Generalizability (The Cross-Domain Verification)

**Question:** Is the "State Blindness" observed in basic arithmetic (`A1`) structurally identical to the failure patterns in Object-Oriented Programming (`A4`)?

**Explanation:**
If the LLM fails to detect "Spreadsheet View" in A1 (Scalar) and "Aliasing" in A4 (Heap), it suggests a fundamental limitation in the Transformer architecture's ability to model _indirection_, regardless of the domain topic.
