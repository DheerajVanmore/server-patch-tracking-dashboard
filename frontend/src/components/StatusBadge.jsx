function StatusBadge({ status }) {
  const getBadgeClass = () => {
    switch (status) {
      case 'ACTIVE':
      case 'PATCHED':
        return 'badge-success';
      case 'MAINTENANCE':
      case 'IN_PROGRESS':
        return 'badge-in-progress';
      case 'PENDING':
        return 'badge-warning';
      case 'OFFLINE':
      case 'FAILED':
        return 'badge-danger';
      case 'EXEMPTED':
        return 'badge-info';
      default:
        return 'badge-neutral';
    }
  };

  return (
    <span className={`badge ${getBadgeClass()}`}>
      {status}
    </span>
  );
}

export default StatusBadge;
