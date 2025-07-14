package com.classycollections.inventory_bookkeeping.auth;

import com.classycollections.inventory_bookkeeping.user.Role;

public record RegisterRequest(String fullName, String email, String password, Role role) {}