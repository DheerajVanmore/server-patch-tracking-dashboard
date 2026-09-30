function EnvironmentBadge({ environment }) {
  const getBadgeClass = () => {
    switch (environment) {
      case 'Production':
        return 'badge-primary';
      case 'Staging':
        return 'badge-purple';
      case 'Development':
        return 'badge-neutral';
      default:
        return 'badge-neutral';
    }
  };

  return (
    <span className={`badge ${getBadgeClass()}`}>
      {environment}
    </span>
  );
}

export default EnvironmentBadge;
