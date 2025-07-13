package com.amsterdam.imageviewer.classification.policy

internal sealed class SafetyPolicy {
    object SFWPolicy : SafetyPolicy()
}