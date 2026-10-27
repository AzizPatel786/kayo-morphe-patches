# Graph Report - Morphe Stuff  (2026-10-27)

## Corpus Check
- 27 files · ~20,310 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 12 file(s) not represented in the graph (top: (none) 6, .properties 2, .xml 1)

## Summary
- 360 nodes · 635 edges · 25 communities (14 shown, 11 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 26 edges (avg confidence: 0.81)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `44d03529`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- gradientdrawable
- KayoPatch.java
- Builder
- generate_patches_readme.py
- Fingerprints.kt
- TrackGroup
- package.json
- CHANGELOG.md
- PatchListGenerator.kt
- KayoPatch.kt
- .getActiveStreamMetrics
- Constants.kt
- ExamplePatch
- gradlew
- Bug Report Template
- rules/graphify.md
- workflows/graphify.md
- Open PR to Main Workflow
- KayoPatch
- viewgroup
- .extractVideoTracks

## God Nodes (most connected - your core abstractions)
1. `KayoPatch` - 92 edges
2. `Builder` - 21 edges
3. `TrackGroup` - 15 edges
4. `Builder` - 13 edges
5. `VideoTrackOption` - 11 edges
6. `ExoPlayer` - 11 edges
7. `Format` - 10 edges
8. `Group` - 10 edges
9. `DefaultTrackSelector` - 10 edges
10. `Tracks` - 9 edges

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

## Communities (25 total, 11 thin omitted)

### Community 2 - "KayoPatch.java"
Cohesion: 0.07
Nodes (28): alertdialog, android.os.Handler, arraylist, clipboardmanager, clipdata, collections, color, comparator (+20 more)

### Community 3 - "Builder"
Cohesion: 0.12
Nodes (4): Builder, DefaultTrackSelector, Parameters, Override

### Community 4 - "generate_patches_readme.py"
Cohesion: 0.13
Nodes (18): anchor(), build_content(), patches_table(), pkg_emoji(), Render a markdown table of supported versions. Experimental versions get a 🧪…, Wrap a patches table in a <details> spoiler with a versions sub-table. If…, Build the full generated patches section., Generates the patches section of README.md from patches-list.json and injects… (+10 more)

### Community 5 - "Fingerprints.kt"
Cohesion: 0.11
Nodes (31): accessflags, fieldaccess, Fingerprint, matchafterimmediately, methodcall, AbstractDaznPlayerControlsOnClickFingerprint, AbstractDaznPlayerControlsSetupFingerprint, AbstractDaznPlayerControlsTrackSelectorVisibilityFingerprint (+23 more)

### Community 6 - "TrackGroup"
Cohesion: 0.09
Nodes (5): Format, TrackGroup, TrackSelectionOverride, Builder, TrackSelectionParameters

### Community 7 - "package.json"
Cohesion: 0.14
Nodes (13): devDependencies, conventional-changelog-conventionalcommits, gradle-semantic-release-plugin, @MorpheApp/changelog, semantic-release, @semantic-release/exec, @semantic-release/git, conventional-changelog-conventionalcommits (+5 more)

### Community 8 - "CHANGELOG.md"
Cohesion: 0.07
Nodes (26): 1.0.0 (2026-10-23), [1.0.1](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.0...v1.0.1) (2026-10-23), [1.0.2](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.1...v1.0.2) (2026-10-23), [1.0.3](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.2...v1.0.3) (2026-10-24), [1.0.4](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.3...v1.0.4) (2026-10-24), [1.0.5](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.4...v1.0.5) (2026-10-24), [1.0.6](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.5...v1.0.6) (2026-10-24), [1.1.0](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.6...v1.1.0) (2026-10-26) (+18 more)

### Community 9 - "PatchListGenerator.kt"
Cohesion: 0.10
Nodes (21): file, gsonbuilder, JSONArray, JSONObject, loadpatchesfromjar, manifest, Patch, generatePatchList() (+13 more)

### Community 10 - "KayoPatch.kt"
Cohesion: 0.14
Nodes (12): addinstruction, addinstructions, addinstructionswithlabels, bytecodepatch, compatibility_kayo_sports, compatibility_kayo_sports_tv, fieldreference, getinstruction (+4 more)

### Community 12 - "Constants.kt"
Cohesion: 0.33
Nodes (5): apkfiletype, apptarget, compatibility, Constants, supportedabi

### Community 14 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 19 - "Bug Report Template"
Cohesion: 1.00
Nodes (3): Bug Report Template, Issue Template Configuration, Feature Request Template

### Community 23 - "KayoPatch"
Cohesion: 0.08
Nodes (10): android.app.Activity, android.content.Context, android.graphics.drawable.GradientDrawable, android.view.View, android.view.ViewGroup, android.widget.TextView, SuppressWarnings, KayoPatch (+2 more)

### Community 25 - ".extractVideoTracks"
Cohesion: 0.13
Nodes (6): AudioTrackOption, VideoTrackOption, Group, Tracks, ImmutableList, Pair

## Knowledge Gaps
- **32 isolated node(s):** `@MorpheApp/changelog`, `@MorpheApp/changelog`, `@semantic-release/exec`, `@semantic-release/exec`, `@semantic-release/git` (+27 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 141 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **11 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `KayoPatch` connect `KayoPatch` to `KayoPatch.java`, `Builder`, `PatchListGenerator.kt`, `.getActiveStreamMetrics`, `.extractVideoTracks`?**
  _High betweenness centrality (0.257) - this node is a cross-community bridge._
- **Why does `TrackGroup` connect `TrackGroup` to `.extractVideoTracks`, `KayoPatch.java`, `Builder`?**
  _High betweenness centrality (0.039) - this node is a cross-community bridge._
- **Why does `DefaultTrackSelector` connect `Builder` to `KayoPatch.java`, `TrackGroup`, `KayoPatch`?**
  _High betweenness centrality (0.035) - this node is a cross-community bridge._
- **What connects `@MorpheApp/changelog`, `@MorpheApp/changelog`, `@semantic-release/exec` to the rest of the system?**
  _32 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `KayoPatch.java` be split into smaller, more focused modules?**
  _Cohesion score 0.06666666666666667 - nodes in this community are weakly interconnected._
- **Should `Builder` be split into smaller, more focused modules?**
  _Cohesion score 0.11954022988505747 - nodes in this community are weakly interconnected._
- **Should `generate_patches_readme.py` be split into smaller, more focused modules?**
  _Cohesion score 0.1286549707602339 - nodes in this community are weakly interconnected._