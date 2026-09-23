function StatusBadge({ status }) {
  const getStatusColor = () => {
    switch (status) {
      case 'ACTIVE':
      case 'PATCHED':
        return { backgroundColor: '#e6f4ea', color: '#137333' }; // Green
      case 'MAINTENANCE':
      case 'IN_PROGRESS':
      case 'PENDING':
        return { backgroundColor: '#fef7e0', color: '#b06000' }; // Yellow
      case 'OFFLINE':
      case 'FAILED':
        return { backgroundColor: '#fce8e6', color: '#c5221f' }; // Red
      case 'EXEMPTED':
        return { backgroundColor: '#e8f0fe', color: '#1a73e8' }; // Blue
      default:
        return { backgroundColor: '#f1f3f4', color: '#5f6368' }; // Gray
    }
  };

  return (
    <span className="badge" style={getStatusColor()}>
      {status}
    </span>
  );
}

export default StatusBadge;
