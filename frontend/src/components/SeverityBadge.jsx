function SeverityBadge({ severity }) {
  const getSeverityColor = () => {
    switch (severity) {
      case 'CRITICAL':
        return { backgroundColor: '#fce8e6', color: '#c5221f' }; // Red
      case 'HIGH':
        return { backgroundColor: '#fef7e0', color: '#b06000' }; // Orange/Amber
      case 'MEDIUM':
        return { backgroundColor: '#fff3e0', color: '#e65100' }; // Yellow
      case 'LOW':
        return { backgroundColor: '#e6f4ea', color: '#137333' }; // Green
      default:
        return { backgroundColor: '#f1f3f4', color: '#5f6368' }; // Gray
    }
  };

  return (
    <span className="badge" style={getSeverityColor()}>
      {severity}
    </span>
  );
}

export default SeverityBadge;
