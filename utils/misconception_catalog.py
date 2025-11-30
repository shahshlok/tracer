"""Misconception catalog for seeded validation.

This module provides access to the catalog of known misconceptions
used for generating seeded submissions and validating detection accuracy.
"""

import json
from dataclasses import dataclass
from pathlib import Path


@dataclass
class Misconception:
    """A known misconception from the catalog."""
    
    id: str
    name: str
    topic: str
    description: str
    applicable_questions: list[str]
    severity: str  # "high", "medium", "low"
    example_incorrect: str
    example_correct: str
    detection_keywords: list[str]
    
    def applies_to(self, question_id: str) -> bool:
        """Check if this misconception applies to a given question."""
        return question_id.lower() in [q.lower() for q in self.applicable_questions]


class MisconceptionCatalog:
    """Manages the catalog of known misconceptions."""
    
    def __init__(self, catalog_path: str = "data/misconception_catalog.json"):
        self.catalog_path = Path(catalog_path)
        self.misconceptions: list[Misconception] = []
        self.version: str = ""
        self._load_catalog()
    
    def _load_catalog(self) -> None:
        """Load the catalog from JSON file."""
        if not self.catalog_path.exists():
            raise FileNotFoundError(f"Catalog not found: {self.catalog_path}")
        
        with open(self.catalog_path) as f:
            data = json.load(f)
        
        self.version = data.get("catalog_version", "unknown")
        
        for item in data.get("misconceptions", []):
            misconception = Misconception(
                id=item["id"],
                name=item["name"],
                topic=item["topic"],
                description=item["description"],
                applicable_questions=item["applicable_questions"],
                severity=item["severity"],
                example_incorrect=item["example_incorrect"],
                example_correct=item["example_correct"],
                detection_keywords=item["detection_keywords"],
            )
            self.misconceptions.append(misconception)
    
    def get_all(self) -> list[Misconception]:
        """Get all misconceptions in the catalog."""
        return self.misconceptions
    
    def get_by_id(self, misconception_id: str) -> Misconception | None:
        """Get a misconception by its ID."""
        for m in self.misconceptions:
            if m.id == misconception_id:
                return m
        return None
    
    def get_by_topic(self, topic: str) -> list[Misconception]:
        """Get all misconceptions for a specific topic."""
        return [m for m in self.misconceptions if m.topic.lower() == topic.lower()]
    
    def get_for_question(self, question_id: str) -> list[Misconception]:
        """Get all misconceptions applicable to a specific question."""
        return [m for m in self.misconceptions if m.applies_to(question_id)]
    
    def get_by_severity(self, severity: str) -> list[Misconception]:
        """Get all misconceptions of a specific severity."""
        return [m for m in self.misconceptions if m.severity.lower() == severity.lower()]
    
    def get_high_severity(self) -> list[Misconception]:
        """Get all high-severity misconceptions (best for validation)."""
        return self.get_by_severity("high")
    
    def summary(self) -> dict:
        """Get a summary of the catalog."""
        topic_counts: dict[str, int] = {}
        severity_counts: dict[str, int] = {}
        question_counts: dict[str, int] = {}
        
        for m in self.misconceptions:
            # Count by topic
            topic_counts[m.topic] = topic_counts.get(m.topic, 0) + 1
            
            # Count by severity
            severity_counts[m.severity] = severity_counts.get(m.severity, 0) + 1
            
            # Count by question
            for q in m.applicable_questions:
                question_counts[q] = question_counts.get(q, 0) + 1
        
        return {
            "version": self.version,
            "total_misconceptions": len(self.misconceptions),
            "by_topic": topic_counts,
            "by_severity": severity_counts,
            "by_question": question_counts,
        }
    
    def print_summary(self) -> None:
        """Print a formatted summary of the catalog."""
        summary = self.summary()
        
        print(f"Misconception Catalog v{summary['version']}")
        print(f"Total: {summary['total_misconceptions']} misconceptions")
        print()
        
        print("By Topic:")
        for topic, count in sorted(summary["by_topic"].items()):
            print(f"  {topic}: {count}")
        print()
        
        print("By Severity:")
        for severity, count in sorted(summary["by_severity"].items()):
            print(f"  {severity}: {count}")
        print()
        
        print("By Question:")
        for question, count in sorted(summary["by_question"].items()):
            print(f"  {question}: {count}")


# Convenience function
def load_catalog(catalog_path: str = "data/misconception_catalog.json") -> MisconceptionCatalog:
    """Load the misconception catalog."""
    return MisconceptionCatalog(catalog_path)


if __name__ == "__main__":
    # Test the catalog
    catalog = load_catalog()
    catalog.print_summary()
    
    print("\n" + "="*50)
    print("High-severity misconceptions:")
    for m in catalog.get_high_severity():
        print(f"  [{m.id}] {m.name} ({m.topic})")
    
    print("\n" + "="*50)
    print("Misconceptions for Q3:")
    for m in catalog.get_for_question("q3"):
        print(f"  [{m.id}] {m.name}")
