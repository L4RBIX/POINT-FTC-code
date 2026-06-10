import { useInView } from '../hooks/useInView';

export default function AnimateIn({
  children,
  className = '',
  delay = 0,
  as: Tag = 'div',
  ...props
}) {
  const [ref, inView] = useInView();

  return (
    <Tag
      ref={ref}
      className={`animate-in ${inView ? 'animate-in--visible' : ''} ${className}`.trim()}
      style={{ transitionDelay: `${delay}ms` }}
      {...props}
    >
      {children}
    </Tag>
  );
}
