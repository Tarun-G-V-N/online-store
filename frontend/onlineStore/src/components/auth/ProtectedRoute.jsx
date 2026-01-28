import React from 'react'
import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useSelector } from 'react-redux'

const ProtectedRoute = ({children, allowedRoles = [], useOutlet = false}) => {
    const location = useLocation();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
    const userRoles = useSelector((state) => state.auth.roles);
    const userRolesLower = userRoles.map((role) => role.toLowerCase());
    const allowedRolesLower = allowedRoles.map((role) => role.toLowerCase());
    const isAuthorized = userRolesLower.some((role) => allowedRolesLower.includes(role));

    if(!isAuthenticated) {
        return <Navigate to={"/login"} state={{from: location}} replace/>
    }

    if(!isAuthorized) {
        return <Navigate to={"/unauthorized"} state={{from: location}} replace/>
    } else {
        return useOutlet ? <Outlet/> : children;
    }
}

export default ProtectedRoute