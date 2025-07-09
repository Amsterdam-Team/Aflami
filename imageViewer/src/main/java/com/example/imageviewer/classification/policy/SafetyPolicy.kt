package com.example.imageviewer.classification.policy

internal sealed class SafetyPolicy {
     object NsfwPolicy : SafetyPolicy()
}