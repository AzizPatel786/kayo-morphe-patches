# Graph Report - Morphe Stuff  (2026-11-06)

## Corpus Check
- 27 files · ~24,840 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 12 file(s) not represented in the graph (top: (none) 6, .properties 2, .xml 1)

## Summary
- 427 nodes · 760 edges · 33 communities (15 shown, 18 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 30 edges (avg confidence: 0.81)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `aeef6399`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .applySelectedQuality
- .extractVideoTracks
- KayoPatch.java
- Builder
- generate_patches_readme.py
- Fingerprints.kt
- android.app.Activity
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
- android.content.Context
- android.graphics.drawable.GradientDrawable
- android.os.Handler
- android.view.View
- android.view.ViewGroup
- android.widget.TextView
- SuppressWarnings
- java.lang.ref.WeakReference

## God Nodes (most connected - your core abstractions)
1. `KayoPatch` - 112 edges
2. `Builder` - 21 edges
3. `Builder` - 13 edges
4. `VideoTrackOption` - 12 edges
5. `TrackGroup` - 12 edges
6. `Group` - 10 edges
7. `AudioTrackOption` - 8 edges
8. `ExoPlayer` - 8 edges
9. `TrackSelectionOverride` - 8 edges
10. `generatePatchList()` - 8 edges

## Surprising Connections (you probably didn't know these)
- `🩹 Patches list` --references--> `main()`  [INFERRED]
  README.md → patches/src/main/kotlin/util/PatchListGenerator.kt
- `Bug Report Template` --semantically_similar_to--> `Feature Request Template`  [INFERRED] [semantically similar]
  .github/ISSUE_TEMPLATE/bug_report.yml → .github/ISSUE_TEMPLATE/feature_request.yml
- `Conventional Changelog Version Pinning` --conceptually_related_to--> `Semantic Release Automation`  [INFERRED]
  .github/dependabot.yml → .github/workflows/release.yml
- `TrackSelectionOverride` --references--> `TrackGroup`  [EXTRACTED]
  extensions/extension/src/main/java/com/google/android/exoplayer2/trackselection/TrackSelectionOverride.java → extensions/extension/src/main/java/com/google/android/exoplayer2/source/TrackGroup.java
- `Parameters` --inherits--> `TrackSelectionParameters`  [EXTRACTED]
  extensions/extension/src/main/java/com/google/android/exoplayer2/trackselection/DefaultTrackSelector.java → extensions/extension/src/main/java/com/google/android/exoplayer2/trackselection/TrackSelectionParameters.java

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **GitHub Issue Template Configuration Suite** — github_issue_template_bug_report, github_issue_template_feature_request, github_issue_template_config [EXTRACTED 1.00]

## Communities (33 total, 18 thin omitted)

### Community 0 - ".applySelectedQuality"
Cohesion: 0.23
Nodes (3): DefaultTrackSelector, ExoPlayer, Tracks

### Community 1 - ".extractVideoTracks"
Cohesion: 0.13
Nodes (8): AudioTrackOption, VideoTrackOption, Group, Tracks, ImmutableList, Format, Pair, TrackGroup

### Community 2 - "KayoPatch.java"
Cohesion: 0.07
Nodes (28): alertdialog, arraylist, clipboardmanager, clipdata, collections, color, comparator, contextwrapper (+20 more)

### Community 3 - "Builder"
Cohesion: 0.06
Nodes (9): TrackGroup, Builder, DefaultTrackSelector, Parameters, TrackSelectionOverride, Builder, TrackSelectionParameters, list (+1 more)

### Community 4 - "generate_patches_readme.py"
Cohesion: 0.13
Nodes (18): anchor(), build_content(), patches_table(), pkg_emoji(), Render a markdown table of supported versions. Experimental versions get a 🧪…, Wrap a patches table in a <details> spoiler with a versions sub-table. If…, Build the full generated patches section., Generates the patches section of README.md from patches-list.json and injects… (+10 more)

### Community 5 - "Fingerprints.kt"
Cohesion: 0.08
Nodes (46): accessflags, fieldaccess, Fingerprint, matchafterimmediately, methodcall, AbstractDaznPlayerControlsOnClickFingerprint, AbstractDaznPlayerControlsSetupFingerprint, AbstractDaznPlayerControlsTrackSelectorVisibilityFingerprint (+38 more)

### Community 7 - "package.json"
Cohesion: 0.14
Nodes (13): devDependencies, conventional-changelog-conventionalcommits, gradle-semantic-release-plugin, @MorpheApp/changelog, semantic-release, @semantic-release/exec, @semantic-release/git, conventional-changelog-conventionalcommits (+5 more)

### Community 8 - "CHANGELOG.md"
Cohesion: 0.04
Nodes (46): 1.0.0 (2026-10-23), [1.0.1](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.0...v1.0.1) (2026-10-23), [1.0.2](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.1...v1.0.2) (2026-10-23), [1.0.3](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.2...v1.0.3) (2026-10-24), [1.0.4](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.3...v1.0.4) (2026-10-24), [1.0.5](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.4...v1.0.5) (2026-10-24), [1.0.6](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.5...v1.0.6) (2026-10-24), [1.1.0](https://github.com/AzizPatel786/kayo-morphe-patches/compare/v1.0.6...v1.1.0) (2026-10-26) (+38 more)

### Community 9 - "PatchListGenerator.kt"
Cohesion: 0.14
Nodes (19): file, gsonbuilder, loadpatchesfromjar, manifest, Patch, generatePatchList(), JsonCompatibility, JsonPatch (+11 more)

### Community 10 - "KayoPatch.kt"
Cohesion: 0.14
Nodes (12): addinstruction, addinstructions, addinstructionswithlabels, bytecodepatch, compatibility_kayo_sports, compatibility_kayo_sports_tv, fieldreference, getinstruction (+4 more)

### Community 11 - ".getActiveStreamMetrics"
Cohesion: 0.12
Nodes (3): StreamMetrics, ExoPlayer, Format

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
Cohesion: 0.07
Nodes (11): Activity, Context, KayoPatch, JSONArray, JSONObject, KeyEvent, OnClickListener, SuppressWarnings (+3 more)

## Knowledge Gaps
- **42 isolated node(s):** `Constants`, `Open PR to Main Workflow`, `Fallback Build Verification`, `Dependabot Configuration`, `graphify` (+37 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 158 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **18 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `KayoPatch` connect `KayoPatch` to `.applySelectedQuality`, `.extractVideoTracks`, `KayoPatch.java`, `.getActiveStreamMetrics`, `.setFieldSafe`?**
  _High betweenness centrality (0.243) - this node is a cross-community bridge._
- **Why does `Builder` connect `Builder` to `.applySelectedQuality`?**
  _High betweenness centrality (0.039) - this node is a cross-community bridge._
- **Why does `TrackGroup` connect `Builder` to `.extractVideoTracks`, `.getActiveStreamMetrics`?**
  _High betweenness centrality (0.025) - this node is a cross-community bridge._
- **What connects `Constants`, `Open PR to Main Workflow`, `Fallback Build Verification` to the rest of the system?**
  _42 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.extractVideoTracks` be split into smaller, more focused modules?**
  _Cohesion score 0.13445378151260504 - nodes in this community are weakly interconnected._
- **Should `KayoPatch.java` be split into smaller, more focused modules?**
  _Cohesion score 0.06896551724137931 - nodes in this community are weakly interconnected._
- **Should `Builder` be split into smaller, more focused modules?**
  _Cohesion score 0.06475485661424607 - nodes in this community are weakly interconnected._