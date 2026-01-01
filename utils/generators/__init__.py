"""Generators for datasets."""

from utils.generators.dataset_generator import app as dataset_app

# For backwards compatibility with pyproject.toml entry point
interactive_main = dataset_app

__all__ = [
    "dataset_app",
    "interactive_main",
]
