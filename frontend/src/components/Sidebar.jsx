import { NavLink } from 'react-router-dom';

function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        Server Patch Tracker
      </div>
      <nav className="nav-links">
        <NavLink to="/" className={({ isActive }) => isActive ? "nav-link active" : "nav-link"}>
          Dashboard
        </NavLink>
        <NavLink to="/servers" className={({ isActive }) => isActive ? "nav-link active" : "nav-link"}>
          Servers
        </NavLink>
        <NavLink to="/patches" className={({ isActive }) => isActive ? "nav-link active" : "nav-link"}>
          Patches
        </NavLink>
        <NavLink to="/patch-events" className={({ isActive }) => isActive ? "nav-link active" : "nav-link"}>
          Patch Events
        </NavLink>
        <NavLink to="/alerts" className={({ isActive }) => isActive ? "nav-link active" : "nav-link"}>
          Alerts
        </NavLink>
      </nav>
    </aside>
  );
}

export default Sidebar;
