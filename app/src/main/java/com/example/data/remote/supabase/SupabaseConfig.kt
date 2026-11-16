package com.example.data.remote.supabase

data class SupabaseConfig(
    val url: String = "https://xyzcompany.supabase.co",
    val anonKey: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.dummy_anon_key_for_archbase",
    val authToken: String? = null
) {
    val isConfigured: Boolean
        get() = url.isNotBlank() &&
                !url.contains("xyzcompany.supabase.co") &&
                anonKey.isNotBlank() &&
                !anonKey.contains("dummy_anon_key")
}
