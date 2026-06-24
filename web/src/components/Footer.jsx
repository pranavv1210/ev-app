export default function Footer() {
  return (
    <footer className="bg-surface-container-lowest w-full py-24 border-t border-glass-border/30 relative z-10">
      <div className="max-w-container-max mx-auto px-margin-mobile flex flex-col md:flex-row justify-between gap-12">
        <div className="max-w-sm">
          <div className="font-headline-md text-headline-md text-primary font-bold mb-6">VoltRide</div>
          <p className="text-text-secondary text-sm leading-relaxed">Pioneering sustainable urban mobility for a cleaner, safer Bengaluru.</p>
        </div>
        <div className="flex gap-24">
          <div>
            <h4 className="text-primary font-bold mb-6 uppercase text-xs tracking-widest">Platform</h4>
            <ul className="space-y-4 text-sm">
              <li><a className="text-text-secondary hover:text-primary transition-colors" href="#models">Models</a></li>
              <li><a className="text-text-secondary hover:text-primary transition-colors" href="#women-mode">Women Mode</a></li>
              <li><a className="text-text-secondary hover:text-primary transition-colors" href="#impact">Impact</a></li>
            </ul>
          </div>
        </div>
      </div>
      <div className="max-w-container-max mx-auto px-margin-mobile mt-16 pt-8 border-t border-glass-border flex flex-col md:flex-row justify-between items-center text-text-secondary text-xs gap-4">
        <p>© 2026 VoltRide Mobility. Crafted for Bengaluru.</p>
        <div className="flex gap-6">
          <span>Bengaluru Operations</span>
          <span className="text-primary-container">Waitlist Status: OPEN</span>
        </div>
      </div>
    </footer>
  );
}
