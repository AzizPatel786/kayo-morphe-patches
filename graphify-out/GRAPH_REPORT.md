# Graph Report - Morphe Stuff  (2026-10-24)

## Corpus Check
- 27 files · ~12,564 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 12 file(s) not represented in the graph (top: (none) 6, .properties 2, .xml 1)

## Summary
- 283 nodes · 446 edges · 24 communities (14 shown, 10 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 18 edges (avg confidence: 0.82)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `ac18028f`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .extractVideoTracks
- KayoPatch
- KayoPatch.java
- Builder
- generate_patches_readme.py
- Fingerprints.kt
- Builder
- package.json
- 1.0.0 (2026-10-23)
- PatchListGenerator.kt
- KayoPatch.kt
- TrackGroup
- Constants.kt
- ExamplePatch
- gradlew
- Bug Report Template
- rules/graphify.md
- workflows/graphify.md
- Open PR to Main Workflow
- .getActiveStreamMetrics

## God Nodes (most connected - your core abstractions)
1. `KayoPatch` - 51 edges
2. `Builder` - 21 edges
3. `TrackGroup` - 15 edges
4. `Builder` - 13 edges
5. `ExoPlayer` - 11 edges
6. `Format` - 10 edges
7. `Group` - 10 edges
8. `DefaultTrackSelector` - 10 edges
9. `TrackSelectionOverride` - 9 edges
10. `VideoTrackOption` - 8 edges

## Surprising Connections (you probably didn't know these)
- `🩹 Patches list` --references--> `main()`  [INFERRED]
  README.md → patches/src/main/kotlin/util/PatchListGenerator.kt
- `Bug Report Template` --semantically_similar_to--> `Feature Request Template`  [INFERRED] [semantically similar]
  .github/ISSUE_TEMPLATE/bug_report.yml → .github/ISSUE_TEMPLATE/feature_request.yml
- `KayoPatch` --references--> `ExoPlayer`  [EXTRACTED]
  extensions/extension/src/main/java/app/template/extension/kayo/KayoPatch.java → extensions/extension/src/main/java/com/google/android/exoplayer2/ExoPlayer.java
- `KayoPatch` --references--> `Tracks`  [EXTRACTED]
  extensions/extension/src/main/java/app/template/extension/kayo/KayoPatch.java → extensions/extension/src/main/java/com/google/android/exoplayer2/Tracks.java
- `KayoPatch` --references--> `DefaultTrackSelector`  [EXTRACTED]
  extensions/extension/src/main/java/app/template/extension/kayo/KayoPatch.java → extensions/extension/src/main/java/com/google/android/exoplayer2/trackselection/DefaultTrackSelector.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **GitHub Issue Template Configuration Suite** — github_issue_template_bug_report, github_issue_template_feature_request, github_issue_template_config [EXTRACTED 1.00]

## Communities (24 total, 10 thin omitted)

### Community 0 - ".extractVideoTracks"
Cohesion: 0.15
Nodes (4): Group, Tracks, DefaultTrackSelector, ImmutableList

### Community 1 - "KayoPatch"
Cohesion: 0.11
Nodes (9): android.app.Activity, android.content.Context, android.view.View, android.widget.TextView, SuppressWarnings, KayoPatch, java.lang.ref.WeakReference, KeyEvent (+1 more)

### Community 2 - "KayoPatch.java"
Cohesion: 0.08
Nodes (24): alertdialog, android.os.Handler, arraylist, clipboardmanager, clipdata, collections, color, comparator (+16 more)

### Community 3 - "Builder"
Cohesion: 0.16
Nodes (4): Builder, Parameters, TrackSelectionOverride, Override

### Community 4 - "generate_patches_readme.py"
Cohesion: 0.13
Nodes (18): anchor(), build_content(), patches_table(), pkg_emoji(), Render a markdown table of supported versions. Experimental versions get a 🧪…, Wrap a patches table in a <details> spoiler with a versions sub-table. If…, Build the full generated patches section., Generates the patches section of README.md from patches-list.json and injects… (+10 more)

### Community 5 - "Fingerprints.kt"
Cohesion: 0.13
Nodes (26): accessflags, fieldaccess, Fingerprint, matchafterimmediately, methodcall, opcode, AbstractDaznPlayerControlsOnClickFingerprint, AbstractDaznPlayerControlsSetupFingerprint (+18 more)

### Community 7 - "package.json"
Cohesion: 0.14
Nodes (13): devDependencies, conventional-changelog-conventionalcommits, gradle-semantic-release-plugin, @MorpheApp/changelog, semantic-release, @semantic-release/exec, @semantic-release/git, conventional-changelog-conventionalcommits (+5 more)

### Community 8 - "1.0.0 (2026-10-23)"
Cohesion: 0.22
Nodes (8): 1.0.0 (2026-10-23), 🐛 Bug Fixes, ✨ New Features, Dependabot Configuration, Conventional Changelog Version Pinning, Release Workflow, Fallback Build Verification, Semantic Release Automation

### Community 9 - "PatchListGenerator.kt"
Cohesion: 0.11
Nodes (21): file, gsonbuilder, JSONArray, JSONObject, loadpatchesfromjar, manifest, Patch, generatePatchList() (+13 more)

### Community 10 - "KayoPatch.kt"
Cohesion: 0.18
Nodes (10): addinstruction, addinstructions, addinstructionswithlabels, bytecodepatch, compatibility_kayo_sports, compatibility_kayo_sports_tv, fieldreference, getinstruction (+2 more)

### Community 11 - "TrackGroup"
Cohesion: 0.19
Nodes (3): VideoTrackOption, Format, TrackGroup

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
- **23 isolated node(s):** `@MorpheApp/changelog`, `@MorpheApp/changelog`, `@semantic-release/exec`, `@semantic-release/exec`, `@semantic-release/git` (+18 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 124 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **10 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `KayoPatch` connect `KayoPatch` to `.extractVideoTracks`, `KayoPatch.java`, `PatchListGenerator.kt`, `TrackGroup`, `.getActiveStreamMetrics`?**
  _High betweenness centrality (0.209) - this node is a cross-community bridge._
- **Why does `TrackGroup` connect `TrackGroup` to `.extractVideoTracks`, `KayoPatch.java`, `Builder`, `Builder`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **Why does `Builder` connect `Builder` to `.extractVideoTracks`, `TrackGroup`?**
  _High betweenness centrality (0.044) - this node is a cross-community bridge._
- **What connects `@MorpheApp/changelog`, `@MorpheApp/changelog`, `@semantic-release/exec` to the rest of the system?**
  _23 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `KayoPatch` be split into smaller, more focused modules?**
  _Cohesion score 0.11025641025641025 - nodes in this community are weakly interconnected._
- **Should `KayoPatch.java` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should `generate_patches_readme.py` be split into smaller, more focused modules?**
  _Cohesion score 0.1286549707602339 - nodes in this community are weakly interconnected._