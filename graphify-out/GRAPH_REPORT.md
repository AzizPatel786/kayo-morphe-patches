# Graph Report - Morphe Stuff  (2026-11-07)

## Corpus Check
- 28 files · ~27,882 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 12 file(s) not represented in the graph (top: (none) 6, .properties 2, .xml 1)

## Summary
- 466 nodes · 920 edges · 47 communities (17 shown, 30 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 46 edges (avg confidence: 0.81)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `74359b43`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Semantic Release Automation
- TrackGroup
- KayoPatch.java
- Builder
- generate_patches_readme.py
- Fingerprints.kt
- 1.0.0 (2026-10-23)
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
- [1.0.2](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.1...v1.0.2) (2026-10-23)
- [1.0.3](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.2...v1.0.3) (2026-10-24)
- [1.0.4](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.3...v1.0.4) (2026-10-24)
- [1.0.5](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.4...v1.0.5) (2026-10-24)
- [1.1.10](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.9...v1.1.10) (2026-10-30)
- Builder
- [1.1.1](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.0...v1.1.1) (2026-10-27)
- [1.1.3](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.2...v1.1.3) (2026-10-27)
- [1.1.4](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.3...v1.1.4) (2026-10-28)
- Tracks
- [1.1.6](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.5...v1.1.6) (2026-10-28)
- [1.1.7](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.6...v1.1.7) (2026-10-30)
- [1.1.8](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.7...v1.1.8) (2026-10-30)
- [1.1.9](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.8...v1.1.9) (2026-10-30)
- [1.2.0](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.11...v1.2.0) (2026-11-06)
- [1.2.1](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.2.0...v1.2.1) (2026-11-06)
- [1.3.0](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.2.1...v1.3.0) (2026-11-06)
- [1.0.1](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.0...v1.0.1) (2026-10-23)
- [1.1.0](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.6...v1.1.0) (2026-10-26)
- [1.4.0](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.3.0...v1.4.0) (2026-11-06)
- ImmutableList
- .of
- TrackSelectionParameters

## God Nodes (most connected - your core abstractions)
1. `KayoPatch` - 116 edges
2. `Builder` - 30 edges
3. `TrackGroup` - 27 edges
4. `Builder` - 18 edges
5. `Tracks` - 16 edges
6. `KayoPatchTest` - 15 edges
7. `DefaultTrackSelector` - 14 edges
8. `TrackSelectionOverride` - 14 edges
9. `VideoTrackOption` - 12 edges
10. `ExoPlayer` - 12 edges

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

## Communities (47 total, 30 thin omitted)

### Community 0 - "Semantic Release Automation"
Cohesion: 0.40
Nodes (5): Dependabot Configuration, Conventional Changelog Version Pinning, Release Workflow, Fallback Build Verification, Semantic Release Automation

### Community 1 - "TrackGroup"
Cohesion: 0.10
Nodes (8): arrays, AudioTrackOption, VideoTrackOption, Format, Override, TrackGroup, Group, Pair

### Community 2 - "KayoPatch.java"
Cohesion: 0.08
Nodes (24): alertdialog, android.graphics.drawable.GradientDrawable, android.os.Handler, arraylist, clipboardmanager, clipdata, color, comparator (+16 more)

### Community 3 - "Builder"
Cohesion: 0.13
Nodes (3): Builder, Override, Parameters

### Community 4 - "generate_patches_readme.py"
Cohesion: 0.13
Nodes (18): anchor(), build_content(), patches_table(), pkg_emoji(), Render a markdown table of supported versions. Experimental versions get a 🧪…, Wrap a patches table in a <details> spoiler with a versions sub-table. If…, Build the full generated patches section., Generates the patches section of README.md from patches-list.json and injects… (+10 more)

### Community 5 - "Fingerprints.kt"
Cohesion: 0.07
Nodes (50): accessflags, fieldaccess, Fingerprint, matchafterimmediately, methodcall, AbstractDaznPlayerControlsOnClickFingerprint, AbstractDaznPlayerControlsSetupFingerprint, AbstractDaznPlayerControlsTrackSelectorVisibilityFingerprint (+42 more)

### Community 6 - "1.0.0 (2026-10-23)"
Cohesion: 0.67
Nodes (3): 1.0.0 (2026-10-23), 🐛 Bug Fixes, ✨ New Features

### Community 7 - "package.json"
Cohesion: 0.14
Nodes (13): devDependencies, conventional-changelog-conventionalcommits, gradle-semantic-release-plugin, @MorpheApp/changelog, semantic-release, @semantic-release/exec, @semantic-release/git, conventional-changelog-conventionalcommits (+5 more)

### Community 8 - "CHANGELOG.md"
Cohesion: 0.22
Nodes (8): [1.0.6](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.5...v1.0.6) (2026-10-24), [1.1.11](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.10...v1.1.11) (2026-11-06), [1.1.2](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.1...v1.1.2) (2026-10-27), [1.1.5](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.1.4...v1.1.5) (2026-10-28), 🐛 Bug Fixes, 🐛 Bug Fixes, 🐛 Bug Fixes, 🐛 Bug Fixes

### Community 9 - "PatchListGenerator.kt"
Cohesion: 0.14
Nodes (19): file, gsonbuilder, loadpatchesfromjar, manifest, Patch, generatePatchList(), JsonCompatibility, JsonPatch (+11 more)

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
Cohesion: 0.06
Nodes (11): android.app.Activity, android.content.Context, android.view.View, android.view.ViewGroup, android.widget.TextView, SuppressWarnings, KayoPatch, JSONArray (+3 more)

### Community 33 - "Tracks"
Cohesion: 0.13
Nodes (8): assert, Tracks, DefaultTrackSelector, KayoPatchTest, field, method, org.junit.Before, org.junit.Test

### Community 44 - "ImmutableList"
Cohesion: 0.24
Nodes (8): collection, collections, ImmutableList, iterator, java.util.AbstractList, linkedhashmap, list, map

## Knowledge Gaps
- **45 isolated node(s):** `@MorpheApp/changelog`, `@MorpheApp/changelog`, `@semantic-release/exec`, `@semantic-release/exec`, `@semantic-release/git` (+40 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 153 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **30 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `KayoPatch` connect `KayoPatch` to `TrackGroup`, `KayoPatch.java`, `.getActiveStreamMetrics`, `Tracks`?**
  _High betweenness centrality (0.236) - this node is a cross-community bridge._
- **Why does `TrackGroup` connect `TrackGroup` to `Tracks`, `KayoPatch.java`, `Builder`, `ImmutableList`, `.of`, `TrackSelectionParameters`, `Builder`?**
  _High betweenness centrality (0.057) - this node is a cross-community bridge._
- **Why does `Builder` connect `Builder` to `Tracks`, `TrackSelectionParameters`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **What connects `@MorpheApp/changelog`, `@MorpheApp/changelog`, `@semantic-release/exec` to the rest of the system?**
  _45 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TrackGroup` be split into smaller, more focused modules?**
  _Cohesion score 0.10372340425531915 - nodes in this community are weakly interconnected._
- **Should `KayoPatch.java` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should `Builder` be split into smaller, more focused modules?**
  _Cohesion score 0.12535612535612536 - nodes in this community are weakly interconnected._