# Graph Report - Morphe Stuff  (2026-10-22)

## Corpus Check
- 26 files · ~9,636 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 12 file(s) not represented in the graph (top: (none) 6, .properties 2, .xml 1)

## Summary
- 246 nodes · 362 edges · 23 communities (15 shown, 8 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 13 edges (avg confidence: 0.82)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `6f9adcf0`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- TrackGroup
- KayoPatch
- KayoPatch.java
- Builder
- generate_patches_readme.py
- Fingerprints.kt
- Builder
- package.json
- Semantic Release Automation
- PatchListGenerator.kt
- KayoPatch.kt
- 👋🧩 Morphe Patches template
- Constants.kt
- ExamplePatch
- gradlew
- Bug Report Template
- rules/graphify.md
- workflows/graphify.md
- Open PR to Main Workflow

## God Nodes (most connected - your core abstractions)
1. `KayoPatch` - 32 edges
2. `Builder` - 21 edges
3. `TrackGroup` - 15 edges
4. `Builder` - 13 edges
5. `Group` - 10 edges
6. `DefaultTrackSelector` - 10 edges
7. `TrackSelectionOverride` - 9 edges
8. `VideoTrackOption` - 8 edges
9. `Format` - 8 edges
10. `Tracks` - 8 edges

## Surprising Connections (you probably didn't know these)
- `Bug Report Template` --semantically_similar_to--> `Feature Request Template`  [INFERRED] [semantically similar]
  .github/ISSUE_TEMPLATE/bug_report.yml → .github/ISSUE_TEMPLATE/feature_request.yml
- `KayoPatch` --references--> `Tracks`  [EXTRACTED]
  extensions/extension/src/main/java/app/template/extension/kayo/KayoPatch.java → extensions/extension/src/main/java/com/google/android/exoplayer2/Tracks.java
- `KayoPatch` --references--> `DefaultTrackSelector`  [EXTRACTED]
  extensions/extension/src/main/java/app/template/extension/kayo/KayoPatch.java → extensions/extension/src/main/java/com/google/android/exoplayer2/trackselection/DefaultTrackSelector.java
- `TrackSelectionOverride` --references--> `TrackGroup`  [EXTRACTED]
  extensions/extension/src/main/java/com/google/android/exoplayer2/trackselection/TrackSelectionOverride.java → extensions/extension/src/main/java/com/google/android/exoplayer2/source/TrackGroup.java
- `Parameters` --inherits--> `TrackSelectionParameters`  [EXTRACTED]
  extensions/extension/src/main/java/com/google/android/exoplayer2/trackselection/DefaultTrackSelector.java → extensions/extension/src/main/java/com/google/android/exoplayer2/trackselection/TrackSelectionParameters.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **GitHub Issue Template Configuration Suite** — github_issue_template_bug_report, github_issue_template_feature_request, github_issue_template_config [EXTRACTED 1.00]

## Communities (23 total, 8 thin omitted)

### Community 0 - "TrackGroup"
Cohesion: 0.11
Nodes (6): VideoTrackOption, Format, TrackGroup, Group, Tracks, ImmutableList

### Community 1 - "KayoPatch"
Cohesion: 0.15
Nodes (7): android.app.Activity, android.content.Context, android.view.View, android.widget.TextView, SuppressWarnings, KayoPatch, OnClickListener

### Community 2 - "KayoPatch.java"
Cohesion: 0.10
Nodes (20): alertdialog, android.os.Handler, arraylist, collections, color, comparator, contextwrapper, dialoginterface (+12 more)

### Community 3 - "Builder"
Cohesion: 0.15
Nodes (4): Builder, DefaultTrackSelector, Parameters, Override

### Community 4 - "generate_patches_readme.py"
Cohesion: 0.13
Nodes (18): anchor(), build_content(), patches_table(), pkg_emoji(), Wrap a patches table in a <details> spoiler with a versions sub-table. If…, Build the full generated patches section., Generates the patches section of README.md from patches-list.json and injects…, Return a standard package emoji regardless of the package name. (+10 more)

### Community 5 - "Fingerprints.kt"
Cohesion: 0.16
Nodes (20): accessflags, fieldaccess, Fingerprint, matchafterimmediately, methodcall, opcode, AbstractDaznPlayerControlsOnClickFingerprint, AbstractDaznPlayerControlsSetupFingerprint (+12 more)

### Community 6 - "Builder"
Cohesion: 0.12
Nodes (3): TrackSelectionOverride, Builder, TrackSelectionParameters

### Community 7 - "package.json"
Cohesion: 0.12
Nodes (15): devDependencies, @cleyrop-org/semantic-release-backmerge, conventional-changelog-conventionalcommits, gradle-semantic-release-plugin, @MorpheApp/changelog, semantic-release, @semantic-release/exec, @semantic-release/git (+7 more)

### Community 8 - "Semantic Release Automation"
Cohesion: 0.33
Nodes (5): Dependabot Configuration, Conventional Changelog Version Pinning, Release Workflow, Fallback Build Verification, Semantic Release Automation

### Community 9 - "PatchListGenerator.kt"
Cohesion: 0.22
Nodes (14): file, gsonbuilder, jsonobject, loadpatchesfromjar, manifest, Patch, generatePatchList(), JsonCompatibility (+6 more)

### Community 10 - "KayoPatch.kt"
Cohesion: 0.18
Nodes (10): addinstruction, addinstructions, addinstructionswithlabels, bytecodepatch, compatibility_kayo_sports, compatibility_kayo_sports_tv, fieldreference, getinstruction (+2 more)

### Community 11 - "👋🧩 Morphe Patches template"
Cohesion: 0.18
Nodes (10): A list of your patches will automatically be shown here after your first patches release is created., ❓ About, 🛠️ Building locally, 🧑‍💻 Dev usage, 🚀 Getting development started, How to use these patches, 📜 License, 👋🧩 Morphe Patches template (+2 more)

### Community 12 - "Constants.kt"
Cohesion: 0.33
Nodes (5): apkfiletype, apptarget, compatibility, Constants, supportedabi

### Community 14 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 19 - "Bug Report Template"
Cohesion: 1.00
Nodes (3): Bug Report Template, Issue Template Configuration, Feature Request Template

## Knowledge Gaps
- **26 isolated node(s):** `@cleyrop-org/semantic-release-backmerge`, `@cleyrop-org/semantic-release-backmerge`, `@MorpheApp/changelog`, `@MorpheApp/changelog`, `@semantic-release/exec` (+21 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 117 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **8 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `KayoPatch` connect `KayoPatch` to `TrackGroup`, `KayoPatch.java`, `Builder`?**
  _High betweenness centrality (0.068) - this node is a cross-community bridge._
- **Why does `TrackGroup` connect `TrackGroup` to `KayoPatch.java`, `Builder`, `Builder`?**
  _High betweenness centrality (0.049) - this node is a cross-community bridge._
- **What connects `@cleyrop-org/semantic-release-backmerge`, `@cleyrop-org/semantic-release-backmerge`, `@MorpheApp/changelog` to the rest of the system?**
  _26 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TrackGroup` be split into smaller, more focused modules?**
  _Cohesion score 0.11375661375661375 - nodes in this community are weakly interconnected._
- **Should `KayoPatch` be split into smaller, more focused modules?**
  _Cohesion score 0.14532019704433496 - nodes in this community are weakly interconnected._
- **Should `KayoPatch.java` be split into smaller, more focused modules?**
  _Cohesion score 0.09523809523809523 - nodes in this community are weakly interconnected._
- **Should `Builder` be split into smaller, more focused modules?**
  _Cohesion score 0.14855072463768115 - nodes in this community are weakly interconnected._