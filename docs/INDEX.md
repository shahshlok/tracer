# 📚 EME Framework Documentation Hub

**Version 2.0.0** - Complete Documentation Index

Welcome to the Ensemble Model Evaluation (EME) Framework documentation! This is your starting point whether you're a complete beginner or an experienced developer.

> **Note**: This is Version 2 - a major redesign focused on research-grade ensemble evaluation and misconception pattern discovery.

---

## 🚀 Start Here (Choose Your Path)

### 👋 I'm New to This Project
**Start with:** [GETTING_STARTED.md](GETTING_STARTED.md)
- Installation and setup (5 minutes)
- Your first evaluation (10 minutes)
- Understanding the basics with visual diagrams

### 👨‍💻 I Want to Develop/Extend
**Start with:** [ARCHITECTURE.md](ARCHITECTURE.md) → [IMPLEMENTATION_GUIDE.md](IMPLEMENTATION_GUIDE.md)
- System design and data flow
- Adding new features and providers
- Code organization

### 🔬 I'm Using This for Research
**Start with:** [RESEARCH_GUIDE.md](RESEARCH_GUIDE.md)
- Research workflows and examples
- Statistical metrics explained
- Publishing your findings

### 🔄 I'm Migrating from Version 1
**Start with:** [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md)
- Breaking changes and updates
- Migration checklist
- Backward compatibility notes

---

## 📖 Documentation Map

### ✅ Available Now

```
docs/
├── INDEX.md                    ← You are here
├── GETTING_STARTED.md          ✅ Installation, first steps, examples
├── SCHEMA_GUIDE.md             ✅ JSON structure, database schema quick reference
├── MIGRATION_GUIDE.md          ✅ v1 → v2 migration guide
│
├── [Legacy v1 Docs]
│   ├── ARCHITECTURE.md         📝 System design (needs v2 update)
│   ├── DATABASE.md             📝 Database docs (v1, being updated)
│   ├── JSON_OUTPUT.md          📝 Old JSON format (deprecated)
│   └── later_CLI_PROVIDER_PLAN.md  📝 Future plans
│
└── [External Docs]
    ├── ../SCHEMA_DOCUMENTATION.md   ✅ Complete schema reference
    ├── ../example.jsonc             ✅ Fully annotated example
    └── ../README.md                 ✅ Project overview
```

### 📋 Planned for Future

- `IMPLEMENTATION_GUIDE.md` - Building features, adding providers
- `RESEARCH_GUIDE.md` - Using EME for research workflows
- `API_REFERENCE.md` - Function/class documentation
- `CONCEPTS.md` - Key terminology and ideas
- `TROUBLESHOOTING.md` - Common issues and solutions

---

## 🎯 What is the EME Framework?

The **Ensemble Model Evaluation** framework helps you:

### 1. Grade Code with Multiple LLMs
```
Student Code → [LLM 1] + [LLM 2] + [LLM 3] → Grades + Feedback
```

### 2. Discover Patterns in Student Work
```
Misconceptions → Pattern Analysis → Insights for Teaching
```

### 3. Research Ensemble Strategies
```
Model A vs B vs C → Statistical Analysis → Which works best?
```

### 4. Get Publication-Ready Data
```
Raw Grades → ICC, Correlations, CI → Research Paper
```

---

## 📊 How It Works (Visual Overview)

```
┌─────────────────────────────────────────────────────────────┐
│  Step 1: INPUT                                              │
│  ┌────────────┐  ┌─────────────┐  ┌───────────────────┐   │
│  │  Question  │  │   Rubric    │  │ Student Code      │   │
│  │  (.md)     │  │   (.json)   │  │ (.java/.py/etc)   │   │
│  └────────────┘  └─────────────┘  └───────────────────┘   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  Step 2: MULTI-MODEL GRADING                                │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   OpenAI     │  │    eduai     │  │ OpenRouter   │     │
│  │  gpt-5-nano  │  │ gpt-oss-120b │  │   gemini     │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
│         │                  │                  │             │
│         └──────────────────┴──────────────────┘             │
│                            │                                │
│                  Each model returns:                        │
│                  • Score                                    │
│                  • Feedback                                 │
│                  • Misconceptions                           │
│                  • Confidence                               │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  Step 3: COMPARISON ANALYSIS                                │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  Statistical Metrics:                                  │ │
│  │  ✓ Inter-rater reliability (ICC, Krippendorff's α)   │ │
│  │  ✓ Model agreement (Pearson, Spearman correlation)    │ │
│  │  ✓ Ensemble decisions (mean, median, weighted)        │ │
│  │  ✓ Confidence intervals & standard error              │ │
│  │  ✓ Model characteristics (strictness, consistency)    │ │
│  └────────────────────────────────────────────────────────┘ │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  Step 4: STORAGE & OUTPUT                                   │
│                                                              │
│  ┌──────────────┐         ┌────────────────────────────┐   │
│  │   SQLite DB  │         │  Evaluation JSON           │   │
│  │  evaluations │  ←───   │  {                         │   │
│  │ misconceptions│         │    context: {...},        │   │
│  └──────────────┘         │    models: {...},         │   │
│                            │    comparison: {...}       │   │
│                            │  }                         │   │
│                            └────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  Step 5: ANALYSIS & INSIGHTS                                │
│                                                              │
│  ┌────────────────┐  ┌────────────────┐  ┌──────────────┐ │
│  │   Dashboard    │  │ LLM Pattern    │  │   Research   │ │
│  │  (for TAs)     │  │   Analysis     │  │    Export    │ │
│  └────────────────┘  └────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

---

## 🆕 What's New in Version 2?

### Major Changes

#### 1. **Research-Grade Statistics**
```
v1: Basic score comparison
v2: ICC, Krippendorff's α, SEM, CI, correlations
```

#### 2. **Misconception Tracking**
```
v1: Simple feedback text
v2: Structured misconceptions with evidence, confidence, patterns
```

#### 3. **Extensible Comparison Metrics**
```
v1: Fixed metrics
v2: Modular metric system - add new ones easily
```

#### 4. **Cleaner Architecture**
```
v1: Tightly coupled LLM calls
v2: Plug-and-play provider system
```

### Breaking Changes
- ⚠️ Evaluation JSON structure completely redesigned
- ⚠️ Database schema updated (evaluations + misconceptions tables)
- ⚠️ LLM service APIs will be refactored (in progress)

See [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) for details.

---

## 🛠️ Quick Reference by Task

| I want to... | Go to... |
|-------------|----------|
| Install and run my first evaluation | ✅ [GETTING_STARTED.md](GETTING_STARTED.md) |
| Learn about the JSON schema | ✅ [SCHEMA_GUIDE.md](SCHEMA_GUIDE.md) or [../SCHEMA_DOCUMENTATION.md](../SCHEMA_DOCUMENTATION.md) |
| See a complete example | ✅ [../example.jsonc](../example.jsonc) |
| Migrate from v1 | ✅ [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) |
| Understand the system architecture | 📝 [ARCHITECTURE.md](ARCHITECTURE.md) (v1, needs update) |
| Understand comparison metrics | ✅ [../SCHEMA_DOCUMENTATION.md#comparison-metrics-reference](../SCHEMA_DOCUMENTATION.md#comparison-metrics-reference) |
| Add a new LLM provider | 📋 Planned: `IMPLEMENTATION_GUIDE.md` |
| Set up for research | 📋 Planned: `RESEARCH_GUIDE.md` |
| Troubleshoot an issue | 📋 Planned: `TROUBLESHOOTING.md` |

---

## 🎓 Learning Paths

### Path 1: Quick Start (30 minutes)
```
1. Read [GETTING_STARTED.md] ✅
   ↓
2. Run your first evaluation
   ↓
3. Explore output with [SCHEMA_GUIDE.md] ✅
```

### Path 2: Understanding the Schema (1 hour)
```
1. Quick reference: [SCHEMA_GUIDE.md] ✅
   ↓
2. Complete details: [../SCHEMA_DOCUMENTATION.md] ✅
   ↓
3. See example: [../example.jsonc] ✅
```

### Path 3: Migrating from v1 (1-2 hours)
```
1. Read [MIGRATION_GUIDE.md] ✅
   ↓
2. Backup your data
   ↓
3. Follow migration steps
   ↓
4. Test with [GETTING_STARTED.md] ✅
```

---

## 💡 Key Concepts (Quick Definitions)

| Term | What It Means |
|------|---------------|
| **Evaluation** | One student's submission graded by all models |
| **Ensemble** | Using multiple models together for better results |
| **Misconception** | A specific misunderstanding in student code |
| **ICC** | Inter-class correlation - measures rater agreement |
| **Comparison** | Statistical analysis comparing model outputs |
| **Provider** | An LLM service (OpenAI, eduai, OpenRouter) |
| **Rubric Category** | One aspect of grading (e.g., "syntax", "logic") |

---

## 🤝 Contributing to Docs

When updating documentation:

✅ **Keep it beginner-friendly** - Assume readers are new
✅ **Use visual diagrams** - ASCII art is perfectly fine
✅ **Provide examples** - Show actual code/JSON
✅ **Link between pages** - Help readers navigate
✅ **Update this index** - When you add new docs
✅ **Mark status** - Use ✅ (exists), 📝 (needs update), 📋 (planned)

---

## 🆘 Getting Help

### Common Issues
1. **Setup problems** → Read [GETTING_STARTED.md](GETTING_STARTED.md)
2. **Schema questions** → Check [SCHEMA_GUIDE.md](SCHEMA_GUIDE.md) or [../SCHEMA_DOCUMENTATION.md](../SCHEMA_DOCUMENTATION.md)
3. **Migration issues** → Follow [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md)
4. **Need an example** → See [../example.jsonc](../example.jsonc)

### Still Stuck?
1. Re-read the relevant guide carefully
2. Check the [example.jsonc](../example.jsonc) for a working example
3. Review the main [README.md](../README.md) for project overview
4. Search the documentation for keywords

---

## 📞 Project Info

**Version:** 2.0.0
**Status:** Active Development (Schema Revamp Phase)
**Researcher:** Shlok Shah
**Institution:** UBC Okanagan
**Course:** COSC 499 - Honours Thesis

See [../README.md#research-context](../README.md#research-context) for citation and contact info.

---

**Last Updated:** November 2024 (Version 2.0.0 Schema Redesign)
**Next Update:** Implementation phase completion

Happy coding! 🚀
