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

## 📖 Complete Documentation Map

```
docs/
├── INDEX.md                    ← You are here
│
├── Getting Started
│   ├── GETTING_STARTED.md      # Installation, first steps, examples
│   └── QUICK_REFERENCE.md      # Command cheat sheet
│
├── Core Concepts
│   ├── ARCHITECTURE.md         # System design, data flow, modules
│   ├── SCHEMA_GUIDE.md         # JSON structure, database schema
│   └── CONCEPTS.md             # Key terminology and ideas
│
├── Developer Guides
│   ├── IMPLEMENTATION_GUIDE.md # Building features, adding providers
│   ├── API_REFERENCE.md        # Function/class documentation
│   └── TESTING.md              # Testing guide
│
├── Research & Analysis
│   ├── RESEARCH_GUIDE.md       # Using EME for research
│   ├── METRICS_EXPLAINED.md    # Statistical metrics guide
│   └── EXAMPLES.md             # Research examples and workflows
│
└── Operations
    ├── MIGRATION_GUIDE.md      # v1 → v2 migration
    ├── DEPLOYMENT.md           # Production setup
    └── TROUBLESHOOTING.md      # Common issues and solutions
```

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
| Install and run my first evaluation | [GETTING_STARTED.md#installation](GETTING_STARTED.md#installation) |
| Understand the system architecture | [ARCHITECTURE.md](ARCHITECTURE.md) |
| Learn about the JSON schema | [SCHEMA_GUIDE.md](SCHEMA_GUIDE.md) or [../SCHEMA_DOCUMENTATION.md](../SCHEMA_DOCUMENTATION.md) |
| Add a new LLM provider | [IMPLEMENTATION_GUIDE.md#adding-llm-providers](IMPLEMENTATION_GUIDE.md#adding-llm-providers) |
| Understand comparison metrics | [METRICS_EXPLAINED.md](METRICS_EXPLAINED.md) |
| Set up for research | [RESEARCH_GUIDE.md#getting-started](RESEARCH_GUIDE.md#getting-started) |
| Migrate from v1 | [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) |
| Troubleshoot an issue | [TROUBLESHOOTING.md](TROUBLESHOOTING.md) |
| Deploy to production | [DEPLOYMENT.md](DEPLOYMENT.md) |

---

## 📚 External Documentation

### Root-Level Docs
- **[SCHEMA_DOCUMENTATION.md](../SCHEMA_DOCUMENTATION.md)** - Complete schema reference with all fields
- **[example.jsonc](../example.jsonc)** - Fully annotated example evaluation
- **[README.md](../README.md)** - Project overview and quick start

### Legacy Docs (v1)
- **[DATABASE.md](DATABASE.md)** - Current database implementation (being updated)
- **[JSON_OUTPUT.md](JSON_OUTPUT.md)** - Old JSON structure (deprecated)
- **[later_CLI_PROVIDER_PLAN.md](later_CLI_PROVIDER_PLAN.md)** - Future plans

---

## 🎓 Learning Paths

### Path 1: Quick Start (30 minutes)
```
1. [GETTING_STARTED.md]
   ↓
2. Run your first evaluation
   ↓
3. Explore the output JSON
```

### Path 2: Developer Onboarding (2 hours)
```
1. [GETTING_STARTED.md]
   ↓
2. [ARCHITECTURE.md] - Understand system design
   ↓
3. [SCHEMA_GUIDE.md] - Master the data model
   ↓
4. [IMPLEMENTATION_GUIDE.md] - Start building
```

### Path 3: Research User (1 hour)
```
1. [GETTING_STARTED.md]
   ↓
2. [RESEARCH_GUIDE.md] - Research workflows
   ↓
3. [METRICS_EXPLAINED.md] - Understand statistics
   ↓
4. Run analyses, export data
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

For complete definitions, see [CONCEPTS.md](CONCEPTS.md).

---

## 🤝 Contributing to Docs

When updating documentation:

✅ **Keep it beginner-friendly** - Assume readers are new
✅ **Use visual diagrams** - ASCII art is perfectly fine
✅ **Provide examples** - Show actual code/JSON
✅ **Link between pages** - Help readers navigate
✅ **Update this index** - When you add new docs

See [CONTRIBUTING.md](../CONTRIBUTING.md) for the full guide.

---

## 🆘 Getting Help

### Common Issues
- Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md) first
- Review [GETTING_STARTED.md](GETTING_STARTED.md) if setup fails
- See [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) if upgrading from v1

### Still Stuck?
1. Check if your question is answered in the relevant guide
2. Look at [../example.jsonc](../example.jsonc) for a working example
3. Review the root [README.md](../README.md)
4. Report an issue with details about what you tried

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
