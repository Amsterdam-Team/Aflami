package com.amsterdam.lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class RemoteDataSourceIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(ApiCallWrapperDetector.ISSUE)

    override val api: Int
        get() = CURRENT_API
}