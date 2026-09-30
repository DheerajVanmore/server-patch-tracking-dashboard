function SeverityBadge({ severity }) {
  const getBadgeClass = () => {
    switch (severity) {
      case 'CRITICAL':
        return 'badge-danger';
      case 'HIGH':
        return 'badge-warning';
      case 'MEDIUM':
        return 'badge-info';
      case 'LOW':
        return 'badge-neutral';
      default:
        return 'badge-neutral';
    }
  };

  return (
    <span className={`badge ${getBadgeClass()}`}>
      {severity}
    </span>
  );
}

export default SeverityBadge;
