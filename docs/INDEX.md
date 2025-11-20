# Documentation Index

Welcome to the Ensemble Model Evaluation Framework documentation! This index helps you find what you need.

## Quick Navigation

### I'm New to This Project

**Start here:** [`00-QUICK-START.md`](00-QUICK-START.md)
- 5-minute installation and first run
- Minimal setup required
- See immediate results

**Then read:** [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md)
- Understand key concepts (models, evaluations, misconceptions)
- Learn how the system works
- Get terminology down

### I Want to Use This for My Assignment

**Go to:** [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)
- Step-by-step evaluation instructions
- Evaluate one student
- Evaluate entire class
- Choose different AI models
- Understand results

### I Want to Understand How It's Built

**Start with:** [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)
- Where every file is located
- What each directory does
- How files relate to each other

**Then read:** [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md)
- System design and data flow
- Design patterns used
- How to extend the system
- Performance considerations

### I Need Technical Reference

**See:** [`04-API-REFERENCE.md`](04-API-REFERENCE.md)
- Complete API documentation
- All functions and parameters
- Data model reference
- Error handling
- Code examples

---

## Document Overview

| Document | Length | Audience | Content |
|----------|--------|----------|---------|
| [`00-QUICK-START.md`](00-QUICK-START.md) | 10 min | Everyone | Install, configure, run |
| [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md) | 20 min | Everyone | Concepts, terminology, workflows |
| [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md) | 30 min | Developers | File organization, module responsibilities |
| [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) | 45 min | Users | How to evaluate students, examples |
| [`04-API-REFERENCE.md`](04-API-REFERENCE.md) | 30 min | Developers | Function signatures, parameters, types |
| [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) | 45 min | Advanced | System design, extension guide, performance |

---

## By Task

### Getting Started

**"I want to try the system right now"**
→ [`00-QUICK-START.md`](00-QUICK-START.md) (5 minutes)

**"I want to understand what this does"**
→ [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md) (20 minutes)

### Using the System

**"How do I evaluate a single student?"**
→ [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) - Section "Evaluating Your First Student"

**"How do I evaluate an entire class?"**
→ [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) - Section "Evaluating Multiple Students"

**"How do I use different AI models?"**
→ [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) - Section "Choosing Different AI Models"

**"How do I understand my results?"**
→ [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) - Section "Understanding Your Results"

### Understanding the Code

**"Where is file X located?"**
→ [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)

**"What does module Y do?"**
→ [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md) - Section "Understanding Each Directory"

**"How does the system work internally?"**
→ [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) - Section "Data Flow Pipeline"

### Development

**"How do I add a new LLM provider?"**
→ [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) - Section "Adding a New LLM Provider"

**"How do I add a new grading strategy?"**
→ [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) - Section "Adding a New Prompting Strategy"

**"What are all the function signatures?"**
→ [`04-API-REFERENCE.md`](04-API-REFERENCE.md)

**"What data structures exist?"**
→ [`04-API-REFERENCE.md`](04-API-REFERENCE.md) - Section "Data Models"

### Troubleshooting

**"It says API key not found"**
→ [`00-QUICK-START.md`](00-QUICK-START.md) - Section "Troubleshooting"

**"What do the evaluation scores mean?"**
→ [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md) - Section "Key Concepts"

**"How do I interpret the JSON results?"**
→ [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) - Section "Understanding Your Results"

---

## By User Role

### I'm a Teacher/Instructor

**Goal:** Grade student code using AI

**Suggested reading order:**
1. [`00-QUICK-START.md`](00-QUICK-START.md) - Get it running (5 min)
2. [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md) - Understand concepts (20 min)
3. [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) - Learn how to evaluate (45 min)

**Key sections to bookmark:**
- "Evaluating Your First Student"
- "Evaluating Multiple Students"
- "Understanding Your Results"

---

### I'm a Developer/Researcher

**Goal:** Understand system, extend functionality, contribute

**Suggested reading order:**
1. [`00-QUICK-START.md`](00-QUICK-START.md) - Get it running (5 min)
2. [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md) - Understand purpose (20 min)
3. [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md) - Learn file organization (30 min)
4. [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) - Understand design (45 min)
5. [`04-API-REFERENCE.md`](04-API-REFERENCE.md) - Reference as needed (30 min)

**Key sections to bookmark:**
- Architecture: Data Flow Pipeline
- Architecture: Module Architecture
- Architecture: Extension Guide

---

### I'm New to Programming

**Goal:** Learn how to use the system

**Suggested reading order:**
1. [`00-QUICK-START.md`](00-QUICK-START.md) - Get running (5 min)
2. [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md) - Learn concepts (20 min)
3. [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md) - See how things are organized (30 min)
4. [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) - Follow step-by-step instructions (45 min)

**Key tip:** Don't worry about the architecture initially. Focus on using the system.

---

## Common Questions

### "How long will this take to set up?"

**Quick setup:** 10-15 minutes
- Install dependencies
- Create `.env` file
- Run example

**Full learning:** 1-2 hours
- Read getting started guide
- Understand the system
- Try evaluating a student

### "Do I need to know Python?"

**For basic usage:** No
- Just copy the example script
- Modify student names/files
- Run it

**For custom development:** Yes
- Understanding Python basics helpful
- Pydantic knowledge useful
- API documentation in Python

### "Which documents should I read?"

**Minimum:** [`00-QUICK-START.md`](00-QUICK-START.md) + [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)

**Recommended:** Add [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md)

**Complete:** Add [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md) + [`04-API-REFERENCE.md`](04-API-REFERENCE.md)

### "Can I extend the system?"

**Yes!** See [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md) - Section "Extension Guide"

You can:
- Add new LLM providers (OpenAI, Claude, custom)
- Add new grading strategies (different prompt types)
- Add new comparison metrics
- Modify existing components

### "What if I get stuck?"

**Step 1:** Check the relevant section above
**Step 2:** Search in the appropriate document
**Step 3:** See "Troubleshooting" in [`00-QUICK-START.md`](00-QUICK-START.md)
**Step 4:** Create an issue on GitHub

---

## Search Guide

**Looking for... → Go to...**

API functions → [`04-API-REFERENCE.md`](04-API-REFERENCE.md)
Architecture → [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md)
Beginner concepts → [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md)
Data models → [`04-API-REFERENCE.md`](04-API-REFERENCE.md) or [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)
Error messages → [`00-QUICK-START.md`](00-QUICK-START.md)
File locations → [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)
How to evaluate students → [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)
How to extend system → [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md)
How to install → [`00-QUICK-START.md`](00-QUICK-START.md)
Module responsibilities → [`02-PROJECT-STRUCTURE.md`](02-PROJECT-STRUCTURE.md)
Performance tips → [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md)
Prompt strategies → [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) or [`05-ARCHITECTURE.md`](05-ARCHITECTURE.md)
Results interpretation → [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)

---

## Document Features

All documents include:

✅ **Clear structure** with table of contents
✅ **Beginner-friendly explanations** (not too technical)
✅ **Practical examples** with code
✅ **Visual diagrams** where helpful
✅ **Navigation links** between documents
✅ **Quick reference** sections
✅ **Common questions** addressed

---

## Document Conventions

**Terminology boxes** - Explain unfamiliar terms
```
A "misconception" is when a student shows a misunderstanding
of a programming concept. Example: Using mutable default arguments.
```

**Code blocks** - Show actual code to copy/use
```python
from pydantic_models import EvaluationDocument
eval_doc = EvaluationDocument(...)
```

**Command blocks** - Commands to run in terminal
```bash
uv run python grade_sergio.py
```

**Diagrams** - Show how components interact
```
Input → Processing → Output
```

**Tips** - Helpful advice
> **Tip:** Use the example script as a template for your evaluations.

**Warnings** - Important information
> **⚠️ Important:** Never commit your `.env` file with API keys!

---

## Feedback & Updates

These documents are maintained along with the project.

**Found a typo or unclear section?**
→ Create an issue on GitHub

**Want to request new documentation?**
→ Create an issue on GitHub

**Want to contribute improvements?**
→ Submit a pull request

---

## Related Resources

**External Links:**
- [Pydantic Documentation](https://docs.pydantic.dev/) - Data validation library
- [OpenAI API Docs](https://platform.openai.com/docs) - OpenAI integration
- [OpenRouter Docs](https://openrouter.ai/docs) - Multi-model support
- [Python Official Docs](https://docs.python.org/3/) - Python language

---

## Quick Start Checklist

- [ ] Read [`00-QUICK-START.md`](00-QUICK-START.md)
- [ ] Install dependencies (`uv sync`)
- [ ] Create `.env` file with API keys
- [ ] Run `uv run python grade_sergio.py`
- [ ] Read [`01-GETTING-STARTED.md`](01-GETTING-STARTED.md)
- [ ] Try [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md) with your own student

---

**Don't know where to start?**
→ Begin with [`00-QUICK-START.md`](00-QUICK-START.md)

**Want to evaluate students now?**
→ Jump to [`03-USAGE-GUIDE.md`](03-USAGE-GUIDE.md)

**Need technical details?**
→ Go to [`04-API-REFERENCE.md`](04-API-REFERENCE.md)
